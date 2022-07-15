package com.htt.bis.dto.course

data class CreateMarkRequest(

    val description : String,
    val studentId : String,
    val topicId : Long,
    val isPlus: Boolean

)
