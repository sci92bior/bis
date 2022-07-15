package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType
import com.htt.bis.dto.database.simple.SimpleEntityDto

data class DestructionDto(
    var id : Long? = null,
    var destructionType : DestructionType? = null,
    var performerId : String? = null,
    var description : String? = null,
    var date: String? = null,
    var place : String? = null,
    var recommendations : String? = null,
    var processItems: List<ProcessItemDto>? = null,
    var seal: Int? = null,
    var obstacleId: Long? = null,
    var explosiveUnitId: Long? = null,
    var secondExplosiveUnitId: Long? = null,
    var go: Boolean? = null,
    var twoStage: Boolean? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var isApproved: Boolean? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
    var additionalItems: List<SimpleEntityDto>? = null,
    var secondAdditionalItems: List<SimpleEntityDto>? = null,
)
