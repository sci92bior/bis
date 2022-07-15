package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateDestructionRequest(
    var destructionType: DestructionType,
    var recommendation: String? = null,
    var twoStage: Boolean,
    var goOnNo:Boolean,
    var explosiveUnitId: Long? = null,
    var secondExplosiveUnitId: Long? = null,
    var obstacleId: Long? = null,
    var performerId : String,
    var description : String? = null,
    var localization : String? = null,
    var additionalItems : List<CreateAdditionalItemRelationRequest>,
    var secondAdditionalItems : List<CreateAdditionalItemRelationRequest>? = null,
    val photosBefore: List< CreatePhotoWithDescriptionRequest>,
    val photosAfter: List< CreatePhotoWithDescriptionRequest>,
    val processItems: List<ProcessItemDto>,
    val mark : Int? = null,
    var date : String? = null,

)
