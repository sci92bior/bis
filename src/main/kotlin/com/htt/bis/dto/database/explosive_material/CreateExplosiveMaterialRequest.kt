package com.htt.bis.dto.database.explosive_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class CreateExplosiveMaterialRequest(
    val name : String,
    val rFactor : Double,
    val grain : Double,
    val unitType : UnitType,
    val photos : List<String>? = null
)

