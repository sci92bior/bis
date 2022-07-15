package com.htt.bis.facade.course

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.*
import com.htt.bis.domain.course.QTopicMark
import com.htt.bis.domain.course.TopicMark
import com.htt.bis.dto.course.CreateMarkRequest
import com.htt.bis.dto.course.TopicDto
import com.htt.bis.dto.course.TopicMarkDto
import com.htt.bis.dto.course.TopicMarkFilter
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Component
class TopicMarkFacade(
    private val topicMarkService: TopicMarkService,
    private val topicService: TopicService,
    private val courseService: CourseService,
    private val authenticationService: AuthenticationService,
    private val keycloakUserService: KeycloakUserService
) {
    var logger: Logger = LoggerFactory.getLogger(TopicMarkFacade::class.java)

    @UserAuthorization
    fun getAllTopicMarks(filter: TopicMarkFilter, pageNo: Int, pageSize: Int, sort: List<String>?): Page<TopicMarkDto> {
        logger.info("Getting page $pageNo from TopicMarks")
        val query = prepareFilterQuery(filter)
        return topicMarkService.getAll(query, pageNo, pageSize, sort).map {
            mapTopicMarkToTopicMarkDto(it)
        }
    }

    @UserAuthorization
    fun getTopicMarkByIds(ids: Array<String>): List<TopicMarkDto> {
        val categories = mutableListOf<TopicMarkDto>()
        ids.forEach { id ->
            val topicMark = getTopicMarkById(id.toLong())
            categories.add(topicMark)
        }
        return categories
    }

    @UserAuthorization
    fun getLast5MarksByCourse(courseId : Long): List<TopicMarkDto> {
        courseService.getById(courseId).ifFound {
            return topicMarkService.getLast5MarkForCourse(it).map{ topicMark ->
                mapTopicMarkToTopicMarkDto(topicMark)
            }
        }
    }

    @UserAuthorization
    fun getLast5MarksByUser(userId : String): List<TopicMarkDto> {
        keycloakUserService.findById(userId).ifFound {
            return topicMarkService.getLast5MarkForUser(userId).map{ topicMark ->
                mapTopicMarkToTopicMarkDto(topicMark)
            }
        }
    }


    @UserAuthorization
    fun getTopicMarkById(id: Long): TopicMarkDto {
        logger.info("Getting TopicMarks with id $id")
        topicMarkService.getById(id).ifFound {
            return mapTopicMarkToTopicMarkDto(it)
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
    fun deleteTopicMark(id: Long) {
        logger.info("Deleting TopicMark with id $id")
        topicMarkService.delete(id)
        logger.info("TopicMark with $id deleted")
    }

    private fun prepareFilterQuery(filter: TopicMarkFilter): BooleanExpression {
        val qTopicMark: QTopicMark = QTopicMark.topicMark

        return qTopicMark.id.isNotNull
            .and(qTopicMark.instructor.eqOrNull(filter.instructorId))
            .and(qTopicMark.studentId.eqOrNull(filter.studentId))
            .and(qTopicMark.isPlus.eqOrNull(filter.isPlus))
            .and(qTopicMark.createDate.goeOrNull(LocalDateTime.ofInstant(
                filter.createDateLater?.let { Instant.ofEpochMilli(it) }, TimeZone
                    .getDefault().toZoneId()
            )))
            .and(qTopicMark.createDate.loeOrNull(LocalDateTime.ofInstant(
                filter.createDateEarlier?.let { Instant.ofEpochMilli(it) }, TimeZone
                    .getDefault().toZoneId()
            )))

    }

    private fun mapTopicMarkToTopicMarkDto(topicMark: TopicMark): TopicMarkDto {
        return TopicMarkDto(
            id = topicMark.id,
            instructorId = topicMark.instructor,
            createDate = topicMark.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            description = topicMark.description,
            isPlus = topicMark.isPlus,
            topic = TopicDto(id = topicMark.topic.id, name = topicMark.topic.name)
        )
    }
}

