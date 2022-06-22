package com.htt.bis.dto.storage

import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.PhotoType
import com.htt.bis.domain.UnitType
import org.springframework.web.multipart.MultipartFile

data class DownloadImageRequest(
    var objectType : ObjectType,
    var photoType : PhotoType,
)
