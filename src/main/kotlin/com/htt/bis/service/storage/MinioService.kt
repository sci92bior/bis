package com.htt.bis.service.storage

import io.minio.*
import mu.KLogging
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*


@Service
class MinioService(
    private val minioClient: MinioClient,
) {

    fun downloadFileFromMinio(bucketName: String, filePath: String): String {

        val stream: InputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(filePath)
                .build()
        )

        val bytes =  IOUtils.toByteArray(stream)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun deleteFileFromMinio(bucketName: String, path: String) {

        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(bucketName).`object`(path).build()
        )
    }

    fun uploadFileToMinio(
        bucketName: String,
        file: MultipartFile,
        fileName: String
    ): String {
        val bucketExists: Boolean = minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        )
        if (bucketExists) {
            //log.info("Warehouse" + bucketName + "Already exists, you can upload files directly.")
        } else {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            )
        }
        if (file.size <= 20971520) {
            minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName)
                    .`object`(fileName)
                    .stream(file.inputStream, file.size, -1)
                    .contentType(file.contentType)
                    .build()
            )

        } else {
            throw Exception("Please upload a attachment smaller than 20 MB")
        }
        return fileName
    }
    companion object : KLogging()
}
