package com.htt.bis.dto.database.build_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class CreateBuildMaterialRequest(
    val name : String,
    val aFactor : Double,
    val photos : List<String>? = null
)

