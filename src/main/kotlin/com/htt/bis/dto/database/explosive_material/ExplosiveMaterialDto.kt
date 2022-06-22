package com.htt.bis.dto.database.explosive_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class ExplosiveMaterialDto(
    var id : Long? = null,
    var name : String? = null,
    var reFactor : Double? = null,
    var grain : Double? = null,
    var unitType : UnitType? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var isApproved: Boolean? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null

)
