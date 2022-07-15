package com.htt.bis.dto.database.simple

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateCategoryRequest(
    var name: String,
    var photos: List<String>
)
