package com.htt.bis.repository

import com.htt.bis.domain.course.Topic
import com.htt.bis.domain.course.TopicMark

interface TopicMarkRepository : JpaRepositoryWithQuerydslPredicate<TopicMark, Long>
