package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.course.Course
import com.htt.bis.domain.course.Topic
import com.htt.bis.service.CrudService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface TopicService : CrudService<Topic>{
}