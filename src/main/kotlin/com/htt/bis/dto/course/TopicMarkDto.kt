package com.htt.bis.dto.course

data class TopicMarkDto(
    val id : Long? = null,
    val createDate : String? = null,
    val description : String? = null,
    val isPlus : Boolean? = null,
    val instructorId : String? = null,
    val participantId : String? = null,
    val topic : TopicDto? = null
)
