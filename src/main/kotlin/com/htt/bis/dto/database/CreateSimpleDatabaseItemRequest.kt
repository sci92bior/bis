package com.htt.bis.dto.database

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class CreateSimpleDatabaseItemRequest(
    val name : String,
    val photoBase64 : String? = null
)

