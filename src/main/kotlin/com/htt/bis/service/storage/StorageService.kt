package com.htt.bis.service.storage

import com.htt.bis.dto.storage.ImageUploadResponse
import com.htt.bis.exception.ImageUploadException
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
class StorageService(
    private val minioService: MinioService,
) {

    fun uploadDatabaseImage(bucketName: String,originalPath: String, thumbnailPath: String, base64Image: String) {

        val decodedBytes: ByteArray = Base64.getDecoder().decode(base64Image.split(",")[1])
        val file = MockMultipartFile("ORIGINAL", "ORIGINAL", "image/jpeg", decodedBytes)
        val thumbnail = createThumbnail(file, )
        try {

            minioService.uploadFileToMinio(
                bucketName,
                file,
                originalPath
            )

            minioService.uploadFileToMinio(
                bucketName,
                thumbnail,
                thumbnailPath
            )

        } catch (e: Exception) {
            throw ImageUploadException()
        }
    }

      private fun createThumbnail(file : MultipartFile) : MultipartFile {
          val thumbaniledImage = Scalr.resize(ImageIO.read(file.inputStream), 100)
          val baos = ByteArrayOutputStream()
          ImageIO.write(thumbaniledImage, "jpeg", baos)
          baos.flush()
          return MockMultipartFile("THUMBNAIL", file.originalFilename, file.contentType, baos.toByteArray())
      }

    fun downloadImage(bucketName: String, path: String): String {
        return minioService.downloadFileFromMinio(bucketName, path)
    }

    fun deletePhoto(bucketName: String, path: String) {
        return minioService.deleteFileFromMinio(bucketName, path)
    }
}
