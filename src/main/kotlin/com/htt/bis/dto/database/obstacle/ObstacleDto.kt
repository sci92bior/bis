package com.htt.bis.dto.database.obstacle

import com.htt.bis.domain.ObstacleType
import com.htt.bis.dto.database.build_material.BuildMaterialQuantityDto

data class ObstacleDto(
    var id : Long? = null,
    var name : String? = null,
    var description : String? = null,
    var thickness : Double? = null,
    var thumbnail : String? = null,
    var obstacleType: ObstacleType? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
    val buildMaterials : List<BuildMaterialQuantityDto>? = null
)
