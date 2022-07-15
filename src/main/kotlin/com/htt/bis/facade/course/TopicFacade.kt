package com.htt.bis.facade.course

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.InstructorAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.common.eqOrNull
import com.htt.bis.domain.course.QTopic
import com.htt.bis.domain.course.Topic
import com.htt.bis.dto.course.TopicDto
import com.htt.bis.dto.course.TopicFilter
import com.htt.bis.dto.course.TopicMarkDto
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.facade.ifFound
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
class TopicFacade(
    private val topicService: TopicService,
    private val topicMarkService: TopicMarkService,
) {
    var logger: Logger = LoggerFactory.getLogger(TopicFacade::class.java)

    @UserAuthorization
    fun getAllTopics(filter: TopicFilter, pageNo: Int, pageSize: Int, sort: List<String>?): Page<TopicDto> {
        logger.info("Getting page $pageNo from Topics")
        val query = prepareFilterQuery(filter)
        return topicService.getAll(query, pageNo, pageSize, sort).map {
            mapTopicToTopicDto(it)
        }
    }

    @UserAuthorization
    fun getTopicById(id: Long): TopicDto {
        logger.info("Getting Topic with id $id")
        topicService.getById(id).ifFound {
            return mapTopicToTopicDto(it)
        }
    }

    @UserAuthorization
    fun getTopicByIds(ids: Array<String>): List<TopicDto> {
        val categories = mutableListOf<TopicDto>()
        ids.forEach { id ->
            val topic = getTopicById(id.toLong())
            categories.add(topic)
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

    @AdminAuthorization
    fun deleteTopic(id: Long) {
        logger.info("Deleting Topic with id $id")
        topicService.delete(id)
        logger.info("Topic with $id deleted")
    }

    private fun prepareFilterQuery(filter: TopicFilter): BooleanExpression {
        val qTopic: QTopic = QTopic.topic

        return qTopic.id.isNotNull
            .and(qTopic.name.containsIgnoreCaseOrNull(filter.q))
            .and(qTopic.course.id.eqOrNull(filter.courseId))

    }

    fun mapTopicToTopicDto(topic: Topic): TopicDto {

        val marks = topic.topicMarks.map {
            TopicMarkDto(createDate = it.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), description = it.description, isPlus = it.isPlus, instructorId = it.instructor)
        }

        return TopicDto(
            id = topic.id,
            name = topic.name,
            endDate = topic.endDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            marks = marks
        )
    }
}