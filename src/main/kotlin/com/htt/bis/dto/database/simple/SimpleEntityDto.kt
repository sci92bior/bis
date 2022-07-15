package com.htt.bis.dto.database.simple

import com.htt.bis.domain.UnitType
import com.htt.bis.domain.core.EntityUnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class SimpleEntityDto(
    var id : Long? = null,
    var name : String? = null,
    var image: String? = null,
    var unitType : String? = null,
    var categoryId: Long? = null,
    var categoryName: String? = null,
    var creationDate : String? = null,
    var createdBy: String? = null


)
