package com.htt.bis.facade.obstacle

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.common.eqOrNull
import com.htt.bis.domain.*
import com.htt.bis.dto.database.obstacle.CreateObstacleRequest
import com.htt.bis.dto.database.build_material.BuildMaterialQuantityDto
import com.htt.bis.dto.database.obstacle.ObstacleDto
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.database.obstacle.ObstacleFilterDTO
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.mapper.BuildMaterialMapper
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.BuildMaterialQuantityService
import com.htt.bis.service.database.BuildMaterialService
import com.htt.bis.service.database.ObstacleService
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
class ObstacleFacade(
    private val obstacleService: ObstacleService,
    private val photoService: PhotoService,
    private val buildMaterialService: BuildMaterialService,
    private val buildMaterialMapper: BuildMaterialMapper,
    private val explosivaMaterialQuantityService: BuildMaterialQuantityService,
    private val storageService: StorageService,
    private val authenticationService: AuthenticationService
) {
    var logger: Logger = LoggerFactory.getLogger(ObstacleFacade::class.java)

    @UserAuthorization
    fun getAllObstacles(obstacleFilterDTO: ObstacleFilterDTO, pageNo: Int, pageSize: Int, sort: List<String>?): Page<ObstacleDto> {
        logger.info("Getting page $pageNo from Obstacles")
        val query = prepareFilterQuery(obstacleFilterDTO)
        return obstacleService.getAll(query, pageNo, pageSize, sort).map {
            mapObstacleToObstacleDto(it)
        }
    }

    @UserAuthorization
    fun getObstacleById(id: Long): ObstacleDto {
        logger.info("Getting Obstacles with id $id")
        obstacleService.getById(id).ifFound {
            return mapObstacleToObstacleDto(it)
        }
    }

    @UserAuthorization
    fun getObstaclePhotos(id: Long): List<PhotoDto> {
        obstacleService.getById(id).ifFound { eu ->
            val photosList  = mutableListOf<PhotoDto>()
            if (eu.photos.isNotEmpty()) {
                eu.photos.forEach {
                   val photo =  PhotoDto(name = it.name, description = it.description, base64 = storageService.downloadImage(ObjectType.OBSTACLE.bucket, it.originalPath))
                    photosList.add(photo)
                }
                 return photosList
            } else {
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun createObstacle(createObstacleRequest: CreateObstacleRequest): Long {
        logger.info("Creating Obstacle with name ${createObstacleRequest.name}")
        val isApproved = authenticationService.isCurrentUserAdmin()
        val currentUser = authenticationService.getCurrentLoggedUserId()

        var thickness = 0.00
        createObstacleRequest.buildMaterials.forEach {
            thickness += it.quantity!!
        }

        val obstacleToSave = Obstacle(
            name = createObstacleRequest.name,
            description = createObstacleRequest.description,
            thickness = thickness,
            obstacleType = createObstacleRequest.obstacleType,
            createdBy = currentUser.toString(),
            creationDate = LocalDateTime.now()
        )
        var savedObstacle = obstacleService.add(obstacleToSave)

        var photoCount = 0
        createObstacleRequest.photos.forEach {
            photoCount++
            photoService.addPhoto(Photo(originalPath = "${savedObstacle.id}/ORIGINAL-$photoCount.jpeg", thumbnailPath = "${savedObstacle.id}/THUMBNAIL-$photoCount.jpeg",
                name = "ObstaclePhoto-${savedObstacle.id}-$photoCount",type="jpeg",uploadedBy = currentUser.toString(),uploadTime = LocalDateTime.now(), obstacle = savedObstacle),it, ObjectType.OBSTACLE)
        }

        val buildMaterialQuantities = createObstacleRequest.buildMaterials.map {
            buildMaterialService.getById(it.buildMaterialId!!).ifFound {buildMaterial ->
                explosivaMaterialQuantityService.add(BuildMaterialQuantity(buildMaterial = buildMaterial,obstacle = savedObstacle, quantity = it.quantity!! ))
            }
        }.toList()

        savedObstacle.buildMaterialQuantity.addAll(buildMaterialQuantities)
        savedObstacle = obstacleService.update(savedObstacle)

        logger.info("Obstacle with name ${createObstacleRequest.name} created")
        return savedObstacle.id!!
    }


    @AdminAuthorization
    fun deleteObstacle(id: Long) {
        logger.info("Deleting Obstacle with id $id")
        obstacleService.delete(id)
        logger.info("Obstacle with $id deleted")
    }

    fun getExplosivesMaterialsById(ids: Array<String>): List<ObstacleDto> {
        val explosives = mutableListOf<ObstacleDto>()
        ids.forEach { id ->
            val explosive = getObstacleById(id.toLong())
            explosives.add(explosive)
        }
        return explosives
    }

    private fun mapObstacleToObstacleDto(obstacle: Obstacle) : ObstacleDto {
        val buildMaterialsQuantity = mutableListOf<BuildMaterialQuantityDto>()
        obstacle.buildMaterialQuantity.forEach {
            val item = BuildMaterialQuantityDto(buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(it.buildMaterial), quantity = it.quantity)
            buildMaterialsQuantity.add(item)
        }

        return ObstacleDto(
            id = obstacle.id,
            name = obstacle.name,
            description = obstacle.description,
            thickness = obstacle.thickness,
            thumbnail = storageService.downloadImage(ObjectType.OBSTACLE.bucket, obstacle.photos.first().thumbnailPath),
            obstacleType = obstacle.obstacleType,
            creationDate = obstacle.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            updateDate = obstacle.updateDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            createdBy = obstacle.createdBy,
            updatedBy = obstacle.updatedBy,
            buildMaterials = buildMaterialsQuantity
        )
    }

    private fun prepareFilterQuery(filter: ObstacleFilterDTO): BooleanExpression {
        val qObstacle: QObstacle = QObstacle.obstacle

        return qObstacle.id.isNotNull
            .and(qObstacle.description.containsIgnoreCaseOrNull(filter.q))
            .and(qObstacle.obstacleType.eqOrNull(filter.obstacleType))
    }
}