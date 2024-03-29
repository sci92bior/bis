package com.htt.bis.editors

import com.fasterxml.jackson.core.JsonProcessingException

import com.fasterxml.jackson.databind.ObjectMapper
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitFilterDTO
import com.htt.bis.dto.database.obstacle.ObstacleFilterDTO
import com.htt.bis.dto.destruction.DestructionFilterDTO

import java.beans.PropertyEditorSupport


class ObstacleFilterEditor : PropertyEditorSupport() {
    private val objectMapper : ObjectMapper = ObjectMapper()
    @Throws(IllegalArgumentException::class)
    override fun setAsText(text: String) {
        if (text.isEmpty()) {
            value = null
        } else {
            var entity: ObstacleFilterDTO?
            try {
                entity = objectMapper.readValue(text, ObstacleFilterDTO::class.java)
            } catch (e: JsonProcessingException) {
                throw IllegalArgumentException(e)
            }
            value = entity
        }
    }
}