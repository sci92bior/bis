package com.htt.bis.dto.database

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class PhotoDto(
    var name : String? = null,
    var base64: String? = null,
    var description: String? = null


)
