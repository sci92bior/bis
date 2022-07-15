package com.htt.bis.dto.course

data class CreateCourseRequest(
    val name : String,
    val instructorId : String,
    val startDate : String,
    val endDate : String,
    val participants : List<String>,
    val topics : List<CreateTopicRequest>
)
