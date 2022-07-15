package com.htt.bis.repository

import com.htt.bis.domain.course.Course
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface CourseRepository : JpaRepositoryWithQuerydslPredicate<Course, Long>{
    fun findByInstructor(instructor : String, pageRequest: PageRequest) : Page<Course>
    fun findByName(name : String) : Course?
}
