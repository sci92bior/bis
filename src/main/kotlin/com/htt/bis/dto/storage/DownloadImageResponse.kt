package com.htt.bis.dto.storage

import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class DownloadImageResponse(
    var content : String,
    var type : String,
)
