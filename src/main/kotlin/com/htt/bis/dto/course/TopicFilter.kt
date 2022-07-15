package com.htt.bis.dto.course

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class TopicFilter(
    var ids : Array<String>? = null,
    var q : String? = null,
    var courseId : Long? = null,
    var instructorId : String? = null,
)
