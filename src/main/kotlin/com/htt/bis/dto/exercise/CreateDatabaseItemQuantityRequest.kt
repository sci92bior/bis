package com.htt.bis.dto.exercise

data class CreateDatabaseItemQuantityRequest(
    var simpleEntityId : Long? = null,
    var explosiveUnitId: Long? = null,
    var quantity: Double
)
