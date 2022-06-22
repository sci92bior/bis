package com.htt.bis.facade.common

import com.htt.bis.service.storage.StorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageFacade(
    private val storageService: StorageService
) {
    var logger: Logger = LoggerFactory.getLogger(ImageFacade::class.java)

}