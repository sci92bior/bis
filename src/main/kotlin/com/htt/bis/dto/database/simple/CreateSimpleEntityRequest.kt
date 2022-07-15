package com.htt.bis.dto.database.simple

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import com.htt.bis.domain.core.EntityUnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateSimpleEntityRequest(
    var name: String,
    var categoryId: Long,
    var unitType : EntityUnitType,
    var photos: List<String>
)
