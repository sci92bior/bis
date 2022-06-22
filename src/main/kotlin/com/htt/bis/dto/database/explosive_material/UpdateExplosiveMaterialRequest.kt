package com.htt.bis.dto.database.explosive_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class UpdateExplosiveMaterialRequest(
    val name : String? = null,
    val reFactor : Double? = null,
    val grain : Double? = null,
    val unitType : UnitType? = null,
    val photoBase64 : String? = null,
    val isApproved : Boolean? = null
)

