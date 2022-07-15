package com.htt.bis.dto.exercise

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateExerciseRequest(
    var name: String,
    var startDate: String,
    var endDate: String,
    var topicId: Long?=null,
    var itemQuantities : List<CreateDatabaseItemQuantityRequest>? =  null,
    var explosiveUnits : List<CreateDatabaseItemQuantityRequest>? = null,
)
