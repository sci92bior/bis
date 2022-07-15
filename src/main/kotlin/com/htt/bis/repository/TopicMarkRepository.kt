package com.htt.bis.repository

import com.htt.bis.domain.course.Course
import com.htt.bis.domain.course.Topic
import com.htt.bis.domain.course.TopicMark
import org.springframework.data.jpa.repository.Query

interface TopicMarkRepository : JpaRepositoryWithQuerydslPredicate<TopicMark, Long>{

    @Query("select t from TopicMark t where t.topic.course.id=?1 and t.studentId=?2")
    fun findAllByStudentIdAndCourseId(courseId : Long,studentId : String) : List<TopicMark>

    fun findAllByStudentIdOrderByCreateDateDesc(studentId : String) : List<TopicMark>

    @Query("select t from TopicMark t where t.topic.course.id=?1 order by t.createDate desc")
    fun findAllByCourse(courseId : Long) : List<TopicMark>
}
