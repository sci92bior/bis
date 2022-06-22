package com.htt.bis.dto.database

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class UpdateSimpleDatabaseItemRequest(
    val name : String? = null,
    val photoBase64 : String? = null,
    val isApproved : Boolean? = null
)

