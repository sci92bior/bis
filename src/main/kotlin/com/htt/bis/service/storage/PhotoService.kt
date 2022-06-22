package com.htt.bis.service.storage

import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.Photo
import com.htt.bis.dto.storage.ImageUploadResponse
import com.htt.bis.exception.ImageUploadException
import com.htt.bis.repository.PhotoRepository
import org.imgscalr.Scalr
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO


@Service
@Transactional
class PhotoService(
    private val storageService: StorageService,
    private val photoRepository: PhotoRepository
) {
    fun addPhoto(photo : Photo, base64Image : String, objectType : ObjectType): Photo{
        storageService.uploadDatabaseImage(objectType.bucket, photo.originalPath, photo.thumbnailPath, base64Image)
        return photoRepository.save(photo)
    }

    fun deletePhoto(photo : Photo, objectType : ObjectType){
        storageService.deletePhoto(objectType.bucket, photo.originalPath)
        storageService.deletePhoto(objectType.bucket, photo.thumbnailPath)
        photoRepository.delete(photo)
    }
}
