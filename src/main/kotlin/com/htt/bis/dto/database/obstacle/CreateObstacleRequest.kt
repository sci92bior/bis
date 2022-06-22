package com.htt.bis.dto.database.obstacle

import com.htt.bis.domain.ObstacleType
import com.htt.bis.dto.database.build_material.CreateBuildMaterialQuantityRequest

data class CreateObstacleRequest(
    var name : String,
    var description : String,
    var thickness : Double,
    var obstacleType : ObstacleType,
    var photos: List<String>,
    val buildMaterials : List<CreateBuildMaterialQuantityRequest>
)
