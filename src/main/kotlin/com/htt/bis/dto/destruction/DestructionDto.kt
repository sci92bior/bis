package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.core.ExpectedEffect
import com.htt.bis.domain.core.MountType

data class DestructionDto(
    var id : Long? = null,
    var destructionType : DestructionType? = null,
    var performerId : String? = null,
    var description : String? = null,
    var date: String? = null,
    var place : String? = null,
    var recommendations : String? = null,
    var mountType: MountType? = null,
    var expectedEffect: ExpectedEffect? = null,
    var processItems: List<ProcessItemDto>? = null,
    var seal: Int? = null,
    var obstacleId: Long? = null,
    var explosiveUnitId: Long? = null,
    var go: Boolean? = null,
    var twoStage: Boolean? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var isApproved: Boolean? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null
)
