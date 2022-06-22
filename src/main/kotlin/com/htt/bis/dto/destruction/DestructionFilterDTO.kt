package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType

data class DestructionFilterDTO(
    var ids : Array<String>? = null,
    var q : String? = null,
    var destructionType : DestructionType? = null,
    var performerId : String? = null,
    var performer : String? = null,
    var place : String? = null,
    var go: Boolean? = null,
    var twoStage: Boolean? = null
)
