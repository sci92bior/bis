package com.htt.bis.service.database

import com.htt.bis.domain.course.Topic
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.TopicRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class TopicServiceImpl(
    private val topicRepository: TopicRepository,
    private val sortService: SortService,
) :TopicService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Topic> {
        val sort = sortService.buildSortFromSortStrings(sortStrings,Topic::class.java)
        return topicRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Topic? {
        return topicRepository.findByIdOrNull(id)
    }

    override fun add(model:Topic):Topic {
        return topicRepository.save(model)
    }

    override fun update(model:Topic):Topic {
        return topicRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            topicRepository.delete(it)
        }
    }

    override fun count() : Long {
       return topicRepository.count()
    }

}
