package com.htt.bis.dto.course

data class CourseDto(
    val id : Long? = null,
    val name : String? = null,
    val instructorId : String? = null,
    val startDate : String? = null,
    val endDate : String? = null,
    val participants : List<CourseParticipantDto>? = null,
    val createdBy : String? = null,
    val creationDate : String? = null,
    val topics : List<TopicDto>? = null,
    val lastMarks: List<TopicMarkDto>? = null
)
