package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class UpdateDestructionRequest(
    var destructionType: DestructionType? = null,
    var recommendation: String? = null,
    var twoStage: Boolean? = null,
    var goOnNo:Boolean? = null,
    var explosiveUnitId: Long? = null,
    var secondExplosiveUnitId: Long? = null,
    var obstacleId: Long? = null,
    var performerId : String? = null,
    var description : String? = null,
    var localization : String? = null,
    var additionalItems : List<CreateAdditionalItemRelationRequest>? = null,
    var secondAdditionalItems : List<CreateAdditionalItemRelationRequest>? = null,
    val photosBefore: List< CreatePhotoWithDescriptionRequest>? = null,
    val photosAfter: List< CreatePhotoWithDescriptionRequest>? = null,
    val processItems: List<ProcessItemDto>? = null,
    val mark : Int? = null,
    var date : String? = null,

)
