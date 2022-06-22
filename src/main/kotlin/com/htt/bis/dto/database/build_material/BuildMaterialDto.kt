package com.htt.bis.dto.database.build_material

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import javax.persistence.Column

data class BuildMaterialDto(
    var id : Long? = null,
    var name : String? = null,
    var aFactor : Double? = null,
    var creationDate: String? = null,
    var updateDate: String? = null,
    var isApproved: Boolean? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null

)
