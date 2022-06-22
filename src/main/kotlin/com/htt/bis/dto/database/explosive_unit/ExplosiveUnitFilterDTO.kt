package com.htt.bis.dto.database.explosive_unit

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType

data class ExplosiveUnitFilterDTO(
    var ids : Array<String>? = null,
    var q : String? = null,
    var explosiveUnitType : ExplosiveUnitType? = null,

)
