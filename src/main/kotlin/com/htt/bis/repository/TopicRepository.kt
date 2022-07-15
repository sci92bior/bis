package com.htt.bis.repository

import com.htt.bis.domain.course.Course
import com.htt.bis.domain.course.Topic

interface TopicRepository : JpaRepositoryWithQuerydslPredicate<Topic, Long>{
    fun findByCourse(course : Course) : List<Topic>
}
