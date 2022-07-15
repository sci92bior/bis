package com.htt.bis.dto.course

data class TopicDto(
    val id : Long? = null,
    val name : String? = null,
    val endDate : String? = null,
    val marks : List<TopicMarkDto>? = null
)
