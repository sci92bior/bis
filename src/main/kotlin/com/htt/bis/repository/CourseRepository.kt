package com.htt.bis.repository

import com.htt.bis.domain.course.Course

interface CourseRepository : JpaRepositoryWithQuerydslPredicate<Course, Long>
