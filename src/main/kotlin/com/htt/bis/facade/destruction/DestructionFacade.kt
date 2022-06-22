package com.htt.bis.facade.destruction

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.common.eqOrNull
import com.htt.bis.domain.*
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.destruction.CreateDestructionRequest
import com.htt.bis.dto.destruction.DestructionDto
import com.htt.bis.dto.destruction.DestructionFilterDTO
import com.htt.bis.dto.destruction.ProcessItemDto
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.DestructionService
import com.htt.bis.service.database.ExplosiveUnitService
import com.htt.bis.service.database.ObstacleService
import com.htt.bis.service.database.ProcessItemService
import com.htt.bis.service.storage.PhotoService
import com.htt.bis.service.storage.StorageService
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class DestructionFacade(
    private val destructionService: DestructionService,
    private val photoService: PhotoService,
    private val storageService: StorageService,
    private val explosiveUnitService: ExplosiveUnitService,
    private val obstacleService: ObstacleService,
    private val authenticationService: AuthenticationService,
    private val processItemService: ProcessItemService
) {
    var logger: Logger = LoggerFactory.getLogger(DestructionFacade::class.java)

    @UserAuthorization
    fun getAllDestructions(destructionFilterDTO: DestructionFilterDTO, page: Int, size: Int, sort: List<String>?): Page<DestructionDto> {
        logger.info("Getting page $page from Destruction")
        val query = prepareFilterQuery(destructionFilterDTO)
        return destructionService.getAll(query, page, size, sort).map {
            mapDestructionToDestructionDto(it)
        }
    }

    @UserAuthorization
    fun getDestructionById(id: Long): DestructionDto {
        logger.info("Getting ExplosiveUnits with id $id")
        destructionService.getById(id).ifFound {
            return mapDestructionToDestructionDto(it)
        }
    }

    @UserAuthorization
    fun getDestructionPhotos(id: Long): Map<String,List<PhotoDto>> {
        destructionService.getById(id).ifFound { eu ->

            val images = mutableMapOf<String, List<PhotoDto>>()
            val photosListBefore = mutableListOf<PhotoDto>()
                eu.photosBefore.forEach {
                    val photo = PhotoDto(name = it.name, description = it.description, base64 = storageService.downloadImage(ObjectType.DESTRUCTIONS.bucket, it.originalPath))
                    photosListBefore.add(photo)
                }
            val photosListAfter = mutableListOf<PhotoDto>()
            eu.photosAfter.forEach {
                val photo = PhotoDto(name = it.name, description = it.description, base64 = storageService.downloadImage(ObjectType.DESTRUCTIONS.bucket, it.originalPath))
                photosListAfter.add(photo)
            }
            images["before"] = photosListBefore
            images["after"] = photosListAfter
            return images
        }
    }

    @UserAuthorization
    fun getDestructionPhotosAfter(id: Long): List<PhotoDto> {
        destructionService.getById(id).ifFound { eu ->
            val photosList = mutableListOf<PhotoDto>()
            if (eu.photosAfter.isNotEmpty()) {
                eu.photosAfter.forEach {
                    val photo = PhotoDto(name = it.name, description = it.description, base64 = storageService.downloadImage(ObjectType.DESTRUCTIONS.bucket, it.originalPath))
                    photosList.add(photo)
                }
                return photosList
            } else {
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun createDestruction(createDestructionRequest: CreateDestructionRequest): Long {
        logger.info("Creating Destruction with name ${createDestructionRequest.localization}")
        val currentUser = authenticationService.getCurrentLoggedUserId()

        var explosiveUnit: ExplosiveUnit? = null
        var obstacle: Obstacle? = null

        if (createDestructionRequest.explosiveUnitId != null) {
            explosiveUnitService.getById(createDestructionRequest.explosiveUnitId!!).ifFound {
                explosiveUnit = it
            }
        }

        if (createDestructionRequest.obstacleId != null) {
            obstacleService.getById(createDestructionRequest.obstacleId!!).ifFound {
                obstacle = it
            }
        }

        val processItems = mutableListOf<ProcessItem>()
        val photosBefore = mutableListOf<Photo>()
        var photosBeforeCount = 0
        val photosAfter = mutableListOf<Photo>()
        var photosAfterCount = 0



        val destructionToSave = Destruction(
            go = createDestructionRequest.goOnNo,
            twoStage = createDestructionRequest.twoStage,
            destructionType = createDestructionRequest.destructionType,
            description = createDestructionRequest.description,
            performer = createDestructionRequest.performerId,
            place = createDestructionRequest.localization,
            date = LocalDateTime.parse(createDestructionRequest.date?.replace("Z", "")),
            createdBy = currentUser.toString(),
            creationDate = LocalDateTime.now(),
            recommendations = createDestructionRequest.recommendation,
            obstacle = obstacle,
            explosiveUnit = explosiveUnit

            )

        var savedDestruction = destructionService.add(destructionToSave)

        createDestructionRequest.photosBefore.forEach {
            photosBeforeCount++
            photoService.addPhoto(Photo(description = it.description,originalPath = "${savedDestruction.id}/BEFORE-ORIGINAL-$photosBeforeCount.jpeg", thumbnailPath = "${savedDestruction.id}/BEFORE-THUMBNAIL-$photosBeforeCount.jpeg",
                name = "DestructionPhotoBefore-${savedDestruction.id}-$photosBeforeCount",type="jpeg",uploadedBy = currentUser.toString(),uploadTime = LocalDateTime.now(), destructionBefore = savedDestruction),it.base64, ObjectType.DESTRUCTIONS)
        }

        createDestructionRequest.photosAfter.forEach {
            photosAfterCount++
            photoService.addPhoto(Photo(description = it.description,originalPath = "${savedDestruction.id}/AFTER-ORIGINAL-$photosAfterCount.jpeg", thumbnailPath = "${savedDestruction.id}/AFTER-THUMBNAIL-$photosAfterCount.jpeg",
                name = "DestructionPhotoAfter-${savedDestruction.id}-$photosAfterCount",type="jpeg",uploadedBy = currentUser.toString(),uploadTime = LocalDateTime.now(), destructionBefore = savedDestruction),it.base64, ObjectType.DESTRUCTIONS)
        }

        createDestructionRequest.processItems.forEach { item ->
            val processItem = processItemService.add(ProcessItem(
                title = item.title!!,
                description = item.description!!,
                time = item.time!!,
                destruction = savedDestruction
            ))
            processItems.add(processItem)
        }

        savedDestruction.photosBefore.addAll(photosBefore)
        savedDestruction.photosAfter.addAll(photosAfter)
        savedDestruction.processItems.addAll(processItems)

        destructionService.update(savedDestruction)
        logger.info("ExplosiveUnit with name ${createDestructionRequest.localization} created")
        return savedDestruction.id!!
    }

    @AdminAuthorization
    fun deleteDestruction(id: Long) {
        logger.info("Deleting Destruction with id $id")
        destructionService.delete(id)
        logger.info("Destruction with $id deleted")
    }

    fun getDestructionsById(ids: Array<String>): List<DestructionDto> {
        val explosives = mutableListOf<DestructionDto>()
        ids.forEach { id ->
            val explosive = getDestructionById(id.toLong())
            explosives.add(explosive)
        }
        return explosives
    }

    private fun mapDestructionToDestructionDto(destruction: Destruction): DestructionDto {

        val processItems = destruction.processItems.map {
            ProcessItemDto(description = it.description, title = it.title, time = it.time)
        }.toList()
        return DestructionDto(
            id = destruction.id,
            description = destruction.description,
            creationDate = destruction.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            updateDate = destruction.updateDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            date = destruction.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            createdBy = destruction.createdBy,
            updatedBy = destruction.updatedBy,
            destructionType = destruction.destructionType,
            performerId = destruction.performer,
            place = destruction.place,
            recommendations = destruction.recommendations,
            mountType = destruction.mountType,
            expectedEffect = destruction.expectedEffect,
            seal = destruction.seal,
            obstacleId = destruction.obstacle?.id,
            explosiveUnitId = destruction.explosiveUnit?.id,
            go = destruction.go,
            processItems = processItems,
            twoStage = destruction.twoStage

            )
    }

    private fun prepareFilterQuery(filter: DestructionFilterDTO): BooleanExpression {
        val qDestruction: QDestruction = QDestruction.destruction

        return qDestruction.id.isNotNull
            .and(qDestruction.description.containsIgnoreCaseOrNull(filter.q))
            .and(qDestruction.place.containsIgnoreCaseOrNull(filter.q))
            .and(qDestruction.destructionType.eqOrNull(filter.destructionType))
            .and(qDestruction.performer.eqOrNull(filter.performerId))
            .and(qDestruction.go.eqOrNull(filter.go))
            .and(qDestruction.twoStage.eqOrNull(filter.twoStage))
    }
}