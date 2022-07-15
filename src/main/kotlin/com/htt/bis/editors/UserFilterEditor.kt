package com.htt.bis.editors

import com.fasterxml.jackson.core.JsonProcessingException

import com.fasterxml.jackson.databind.ObjectMapper
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.SimpleEntityFilter
import com.htt.bis.dto.user.UserFilter

import java.beans.PropertyEditorSupport


class UserFilterEditor : PropertyEditorSupport() {
    private val objectMapper : ObjectMapper = ObjectMapper()
    @Throws(IllegalArgumentException::class)
    override fun setAsText(text: String) {
        if (text.isEmpty()) {
            value = null
        } else {
            var entity: UserFilter?
            try {
                entity = objectMapper.readValue(text, UserFilter::class.java)
            } catch (e: JsonProcessingException) {
                throw IllegalArgumentException(e)
            }
            value = entity
        }
    }
}