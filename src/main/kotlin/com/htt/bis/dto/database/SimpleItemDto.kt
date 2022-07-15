package com.htt.bis.dto.database

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class SimpleItemDto(
    var id : Long? = null,
    var name : String? = null,
    var isApproved: Boolean? = null,
)
