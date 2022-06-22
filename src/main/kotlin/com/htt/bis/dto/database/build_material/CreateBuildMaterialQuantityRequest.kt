package com.htt.bis.dto.database.build_material

data class CreateBuildMaterialQuantityRequest(
    var buildMaterialId : Long? = null,
    var quantity : Double? = null
)
