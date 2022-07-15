package com.htt.bis.service.database

import com.htt.bis.domain.course.Course
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.CourseRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    private val sortService: SortService,
) :CourseService {
    override fun getByInstructor(instructorId: String, pageRequest: PageRequest): Page<Course>? {
        return courseRepository.findByInstructor(instructorId, pageRequest)
    }

    override fun getByName(name: String): Course? {
        return courseRepository.findByName(name)
    }

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Course> {
        val sort = sortService.buildSortFromSortStrings(sortStrings,Course::class.java)
        return courseRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Course? {
        return courseRepository.findByIdOrNull(id)
    }

    override fun add(model:Course):Course {
        if(courseRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return courseRepository.save(model)
    }

    override fun update(model:Course):Course {
        return courseRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            courseRepository.delete(it)
        }
    }

    override fun count() : Long {
       return courseRepository.count()
    }

}
