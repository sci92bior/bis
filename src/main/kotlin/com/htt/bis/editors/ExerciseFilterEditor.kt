package com.htt.bis.editors

import com.fasterxml.jackson.core.JsonProcessingException

import com.fasterxml.jackson.databind.ObjectMapper
import com.htt.bis.dto.course.CourseFilter
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.exercise.ExerciseFilterDTO

import java.beans.PropertyEditorSupport


class ExerciseFilterEditor : PropertyEditorSupport() {
    private val objectMapper : ObjectMapper = ObjectMapper()
    @Throws(IllegalArgumentException::class)
    override fun setAsText(text: String) {
        if (text.isEmpty()) {
            value = null
        } else {
            var entity: ExerciseFilterDTO?
            try {
                entity = objectMapper.readValue(text, ExerciseFilterDTO::class.java)
            } catch (e: JsonProcessingException) {
                throw IllegalArgumentException(e)
            }
            value = entity
        }
    }
}