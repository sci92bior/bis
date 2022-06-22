package com.htt.bis.dto.database

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class EntityFilter(
    var ids : Array<String>? = null,
    var q : String? = null
)
