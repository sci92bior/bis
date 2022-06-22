package com.htt.bis.dto.database.build_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class UpdateBuildMaterialRequest(
    val name : String? = null,
    val aFactor : Double? = null,
    val photoBase64 : String? = null,
    val isApproved : Boolean? = null
)

