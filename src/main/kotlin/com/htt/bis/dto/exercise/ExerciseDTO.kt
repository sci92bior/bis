package com.htt.bis.dto.exercise

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.ObstacleType
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import com.htt.bis.dto.course.TopicDto

data class ExerciseDTO(
    var id : Long? = null,
    var name : String? = null,
    var startDate : String? = null,
    var endDate : String? = null,
    var createdBy : String? = null,
    var topic : TopicDto? = null,
    var databaseItems : List<DatabaseItemQuantityDto>? = null


    )
