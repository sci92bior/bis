package com.htt.bis.dto.storage

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class ImageUploadResponse(
    var original : String,
    var thumbnail : String,
)
