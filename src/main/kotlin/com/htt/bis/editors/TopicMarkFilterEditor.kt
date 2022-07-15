package com.htt.bis.editors

import com.fasterxml.jackson.core.JsonProcessingException

import com.fasterxml.jackson.databind.ObjectMapper
import com.htt.bis.dto.course.CourseFilter
import com.htt.bis.dto.course.TopicMarkFilter
import com.htt.bis.dto.database.EntityFilter

import java.beans.PropertyEditorSupport


class TopicMarkFilterEditor : PropertyEditorSupport() {
    private val objectMapper : ObjectMapper = ObjectMapper()
    @Throws(IllegalArgumentException::class)
    override fun setAsText(text: String) {
        if (text.isEmpty()) {
            value = null
        } else {
            var entity: TopicMarkFilter?
            try {
                entity = objectMapper.readValue(text, TopicMarkFilter::class.java)
            } catch (e: JsonProcessingException) {
                throw IllegalArgumentException(e)
            }
            value = entity
        }
    }
}