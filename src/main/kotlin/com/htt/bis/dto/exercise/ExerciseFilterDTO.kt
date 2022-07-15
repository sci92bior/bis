package com.htt.bis.dto.exercise

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.ObstacleType

data class ExerciseFilterDTO(
    var ids : Array<String>? = null,
    var q : String? = null,
    var startDateLater : String? = null,
    var endDateEarlier : String? = null,
    var startDateEarlier : String? = null,
    var endDateLater : String? = null,

)
