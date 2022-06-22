package com.htt.bis.dto.database.explosive_unit

import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.dto.database.explosive_material.CreateExplosiveMaterialQuantityRequest

data class CreateExplosiveUnitRequest(
    var name : String,
    var description : String,
    var makeTime : Double,
    var explosiveUnitType : ExplosiveUnitType,
    var photos: List<String>,
    val explosiveMaterials : List<CreateExplosiveMaterialQuantityRequest>
)
