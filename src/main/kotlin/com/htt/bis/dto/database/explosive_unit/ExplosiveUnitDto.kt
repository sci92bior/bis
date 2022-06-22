package com.htt.bis.dto.database.explosive_unit

import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.dto.database.explosive_material.ExplosiveMaterialQuantityDto

data class ExplosiveUnitDto(
    var id : Long? = null,
    var name : String? = null,
    var description : String? = null,
    var newActual : Double? = null,
    var newTnt : Double? = null,
    var makeTime : Double? = null,
    var thumbnail : String? = null,
    var msd : Double? = null,
    var explosiveUnitType : ExplosiveUnitType? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
    val explosiveMaterials : List<ExplosiveMaterialQuantityDto>? = null
)
