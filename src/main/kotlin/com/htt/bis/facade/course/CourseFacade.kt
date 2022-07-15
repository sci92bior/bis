package com.htt.bis.facade.course

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.InstructorAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.domain.course.Course
import com.htt.bis.domain.course.QCourse
import com.htt.bis.domain.course.Topic
import com.htt.bis.domain.course.TopicMark
import com.htt.bis.dto.course.*
import com.htt.bis.exception.CourseAlreadyExistException
import com.htt.bis.exception.UserNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.KeycloakUserService
import com.htt.bis.service.database.CourseService
import com.htt.bis.service.database.TopicMarkService
import com.htt.bis.service.database.TopicService
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class CourseFacade(
    private val courseService: CourseService,
    private val topicService: TopicService,
    private val keycloakUserService: KeycloakUserService,
    private val authenticationService: AuthenticationService,
    private val topicMarkService: TopicMarkService
) {
    var logger: Logger = LoggerFactory.getLogger(CourseFacade::class.java)

    @UserAuthorization
    fun getAllCourses(filter: CourseFilter, pageNo: Int, pageSize: Int, sort: List<String>?): Page<CourseDto> {
        logger.info("Getting page $pageNo from Courses")
        val query = prepareFilterQuery(filter)
        return courseService.getAll(query, pageNo, pageSize, sort).map {
            mapCourseToCourseDto(it)
        }
    }

    @UserAuthorization
    fun getCoursesById(ids: Array<String>): List<CourseDto> {
        val categories = mutableListOf<CourseDto>()
        ids.forEach { id ->
            val course = getCourseById(id.toLong())
            categories.add(course)
        }
        return categories
    }

    @InstructorAuthorization
    fun markTopicAsEnded(topicId : Long) {
       topicService.getById(topicId).ifFound {
           val topicToSave = it.copy(endDate = LocalDateTime.now())
           topicService.update(topicToSave)
       }
    }

    @UserAuthorization
    fun getCourseById(id: Long): CourseDto {
        logger.info("Getting Courses with id $id")
        courseService.getById(id).ifFound {
            return mapCourseToCourseDto(it)
        }
    }

    @UserAuthorization
    fun updateCourse(courseDto: CourseDto): CourseDto {
        logger.info("Updating Courses with id ${courseDto.id}")
        courseService.getById(courseDto.id!!).ifFound {
            return mapCourseToCourseDto(courseService.update(it))
        }
    }

    @UserAuthorization
    fun addMarkToTopic(createMarkRequest : CreateMarkRequest) {
        val user = authenticationService.getCurrentLoggedUserId()
        topicService.getById(createMarkRequest.topicId).ifFound {
            topicMarkService.add(
                TopicMark(
                    createDate = LocalDateTime.now(),
                    description = createMarkRequest.description,
                    isPlus = createMarkRequest.isPlus,
                    topic = it,
                    studentId = createMarkRequest.studentId,
                    instructor = user.toString()
                )
            )
        }
    }

    @AdminAuthorization
    fun createCourse(createCourseRequest: CreateCourseRequest): CourseDto {
        logger.info("Creating Course with name ${createCourseRequest.name}")
        val userId = authenticationService.getCurrentLoggedUserId()
        try {
            createCourseRequest.participants.forEach {
                keycloakUserService.findById(it)
            }
            keycloakUserService.findById(createCourseRequest.instructorId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }

        if (courseService.getByName(createCourseRequest.name) != null) {
            throw CourseAlreadyExistException()
        }

        val courseToSave = Course(
            name = createCourseRequest.name,
            startDate = LocalDateTime.parse(createCourseRequest.startDate.replace("Z", "")),
            createdBy = userId.toString(),
            instructor = createCourseRequest.instructorId,
            endDate = LocalDateTime.parse(createCourseRequest.endDate.replace("Z", "")),
            creationDate = LocalDateTime.now(),
            participants = createCourseRequest.participants.toSet()

        )
        val savedCourse = courseService.add(courseToSave)

        createCourseRequest.topics.forEach {
            topicService.add(Topic(name = it.name, course = savedCourse))
        }

        return mapCourseToCourseDto(savedCourse)
    }


    @AdminAuthorization
    fun deleteCourse(id: Long) {
        logger.info("Deleting Course with id $id")
        courseService.delete(id)
        logger.info("Course with $id deleted")
    }

    private fun prepareFilterQuery(filter: CourseFilter): BooleanExpression {
        val qCourse: QCourse = QCourse.course

        return qCourse.id.isNotNull
            .and(qCourse.name.containsIgnoreCaseOrNull(filter.q))

    }

    private fun mapCourseToCourseDto(course: Course): CourseDto {

        val participantsMarks = mutableListOf<CourseParticipantDto>()
        course.participants.forEach { participantId ->
            val marks = topicMarkService.findByCourseAndStudent(course, participantId)
            participantsMarks.add(
                CourseParticipantDto(
                    participantId = participantId,
                    pluses = marks.count { it.isPlus },
                    minuses = marks.count { !it.isPlus },
                    marks = marks.map { mark ->
                        TopicMarkDto(
                            id = mark.id,
                            createDate = mark.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            description = mark.description,
                            isPlus = mark.isPlus,
                            instructorId = mark.instructor
                        )
                    })
            )
        }

        val marks = topicMarkService.getLast5MarkForCourse(course).map {
            TopicMarkDto(
                id = it.id,
                participantId = it.studentId,
                instructorId = it.instructor,
                createDate = it.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                description = it.description,
                isPlus = it.isPlus,
                topic = TopicDto(id = it.topic.id, name = it.topic.name)
            )
        }
        val topics = mutableListOf<TopicDto>()
        course.topics.forEach {
            topics.add(TopicDto(id=it.id,name = it.name, endDate = it.endDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),))
        }

        return CourseDto(
            id = course.id,
            name = course.name,
            instructorId = course.instructor,
            startDate = course.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            endDate = course.endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            participants = participantsMarks,
            createdBy = course.createdBy,
            topics = topics,
            lastMarks = marks,
            creationDate = course.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
        )
    }
}