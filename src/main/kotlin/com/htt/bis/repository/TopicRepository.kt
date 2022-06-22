package com.htt.bis.repository

import com.htt.bis.domain.course.Topic

interface TopicRepository : JpaRepositoryWithQuerydslPredicate<Topic, Long>
