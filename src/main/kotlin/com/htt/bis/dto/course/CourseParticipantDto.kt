package com.htt.bis.dto.course

data class CourseParticipantDto(
    val participantId : String? = null,
    val marks : List<TopicMarkDto>? = null,
    val pluses : Int? = null,
    val minuses: Int? = null
)
