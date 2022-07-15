package com.htt.bis.dto.exercise

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitDto
import com.htt.bis.dto.database.simple.SimpleEntityDto

data class DatabaseItemQuantityDto(
    var simpleEntityId : Long? = null,
    var explosiveUnitId: Long? = null,
    var explosiveMaterialId: Long? = null,
    var quantity: Double? = null,
    var name : String? = null,
    var unit : String? = null
)