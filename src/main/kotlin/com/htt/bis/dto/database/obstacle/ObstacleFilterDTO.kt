package com.htt.bis.dto.database.obstacle

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.ObstacleType

data class ObstacleFilterDTO(
    var ids : Array<String>? = null,
    var q : String? = null,
    var obstacleType: ObstacleType? = null,

)
