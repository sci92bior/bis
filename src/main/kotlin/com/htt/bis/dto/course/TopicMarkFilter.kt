package com.htt.bis.dto.course

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class TopicMarkFilter(
    var ids : Array<String>? = null,
    var createDateLater : Long? = null,
    var instructorId : String? = null,
    var isPlus: Boolean? = null,
    val studentId : String? = null,
    val createDateEarlier : Long? = null
)
