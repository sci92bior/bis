package com.htt.bis.dto.destruction

import com.htt.bis.domain.DestructionType
import com.htt.bis.domain.ExplosiveUnitType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreatePhotoWithDescriptionRequest(
    val base64: String,
    val description : String,

)
