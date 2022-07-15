package com.htt.bis.service.database

import com.htt.bis.domain.course.Course
import com.htt.bis.domain.course.TopicMark
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.TopicMarkRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class TopicMarkServiceImpl(
    private val topicMarkRepository: TopicMarkRepository,
    private val sortService: SortService,
) :TopicMarkService {
    override fun findByCourseAndStudent(course: Course, studentId: String): List<TopicMark> {
        return topicMarkRepository.findAllByStudentIdAndCourseId(course.id!!, studentId)
    }

    override fun getLast5MarkForUser(studentId: String): List<TopicMark> {
        val list = topicMarkRepository.findAllByStudentIdOrderByCreateDateDesc(studentId)
        return if(list.size >5){
            list.subList(0, 4)
        }else{
            list
        }
    }

    override fun getLast5MarkForCourse(course: Course): List<TopicMark> {
        val list =  topicMarkRepository.findAllByCourse(course.id!!)
        return if(list.size >5){
            list.subList(0, 4)
        }else{
            list
        }
    }


    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<TopicMark> {
        val sort = sortService.buildSortFromSortStrings(sortStrings,TopicMark::class.java)
        return topicMarkRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): TopicMark? {
        return topicMarkRepository.findByIdOrNull(id)
    }

    override fun add(model:TopicMark):TopicMark {
        return topicMarkRepository.save(model)
    }

    override fun update(model:TopicMark):TopicMark {
        return topicMarkRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            topicMarkRepository.delete(it)
        }
    }

    override fun count() : Long {
       return topicMarkRepository.count()
    }

}
