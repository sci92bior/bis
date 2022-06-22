package com.htt.bis.facade.explosive_unit

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.common.eqOrNull
import com.htt.bis.domain.*
import com.htt.bis.dto.database.explosive_unit.CreateExplosiveUnitRequest
import com.htt.bis.dto.database.explosive_material.ExplosiveMaterialQuantityDto
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitDto
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitFilterDTO
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.mapper.ExplosiveMaterialMapper
import com.htt.bis.mapper.ExplosiveUnitMapper
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.ExplosiveMaterialQuantityService
import com.htt.bis.service.database.ExplosiveMaterialService
import com.htt.bis.service.database.ExplosiveUnitService
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
class ExplosiveUnitFacade(
    private val explosiveUnitService: ExplosiveUnitService,
    private val explosiveUnitMapper: ExplosiveUnitMapper,
    private val photoService: PhotoService,
    private val explosiveMaterialService: ExplosiveMaterialService,
    private val explosiveMaterialMapper: ExplosiveMaterialMapper,
    private val explosivaMaterialQuantityService: ExplosiveMaterialQuantityService,
    private val storageService: StorageService,
    private val authenticationService: AuthenticationService
) {
    var logger: Logger = LoggerFactory.getLogger(ExplosiveUnitFacade::class.java)

    @UserAuthorization
    fun getAllExplosivesUnits(explosiveUnitFilterDTO: ExplosiveUnitFilterDTO, pageNo: Int, pageSize: Int, sort: List<String>?): Page<ExplosiveUnitDto> {
        logger.info("Getting page $pageNo from ExplosiveUnits")
        val query = prepareFilterQuery(explosiveUnitFilterDTO)
        return explosiveUnitService.getAll(query, pageNo, pageSize, sort).map {
            mapExplosiveUnitToExplosiveUnitDto(it)
        }
    }

    @UserAuthorization
    fun getExplosiveUnitById(id: Long): ExplosiveUnitDto {
        logger.info("Getting ExplosiveUnits with id $id")
        explosiveUnitService.getById(id).ifFound {
            return mapExplosiveUnitToExplosiveUnitDto(it)
        }
    }

    @UserAuthorization
    fun getExplosiveUnitPhotos(id: Long): List<PhotoDto> {
        explosiveUnitService.getById(id).ifFound { eu ->
            val photosList  = mutableListOf<PhotoDto>()
            if (eu.photos.isNotEmpty()) {
                eu.photos.forEach {
                   val photo =  PhotoDto(name = it.name, description = it.description, base64 = storageService.downloadImage(ObjectType.EXPLOSIVE_UNITS.bucket, it.originalPath))
                    photosList.add(photo)
                }
                 return photosList
            } else {
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun createExplosiveUnit(createExplosiveUnitRequest: CreateExplosiveUnitRequest): Long {
        logger.info("Creating ExplosiveUnit with name ${createExplosiveUnitRequest.name}")
        val isApproved = authenticationService.isCurrentUserAdmin()
        val currentUser = authenticationService.getCurrentLoggedUserId()

        val explosiveUnitToSave = ExplosiveUnit(
            name = createExplosiveUnitRequest.name,
            description = createExplosiveUnitRequest.description,
            newActual = 1.00,
            newTnt = 2.00,
            makeTime = createExplosiveUnitRequest.makeTime,
            msd = 2.00,
            explosiveUnitType = createExplosiveUnitRequest.explosiveUnitType,
            createdBy = currentUser.toString(),
            creationDate = LocalDateTime.now()
        )
        var savedExplosiveUnit = explosiveUnitService.add(explosiveUnitToSave)

        var photoCount = 0
        createExplosiveUnitRequest.photos.forEach {
            photoCount++
            photoService.addPhoto(Photo(originalPath = "${savedExplosiveUnit.id}/ORIGINAL-$photoCount.jpeg", thumbnailPath = "${savedExplosiveUnit.id}/THUMBNAIL-$photoCount.jpeg",
                name = "ExplosiveUnitPhoto-${savedExplosiveUnit.id}-$photoCount",type="jpeg",uploadedBy = currentUser.toString(),uploadTime = LocalDateTime.now(), explosiveUnit = savedExplosiveUnit),it, ObjectType.EXPLOSIVE_UNITS)
        }

        val explosiveUnitQuantities = createExplosiveUnitRequest.explosiveMaterials.map {
            explosiveMaterialService.getById(it.explosiveMaterialId!!).ifFound {explosiveMaterial ->
                explosivaMaterialQuantityService.add(ExplosiveMaterialQuantity(explosiveMaterial = explosiveMaterial,explosiveUnit = savedExplosiveUnit, quantity = it.quantity!! ))
            }
        }.toList()

        savedExplosiveUnit.explosiveMaterials.addAll(explosiveUnitQuantities)
        savedExplosiveUnit = explosiveUnitService.update(savedExplosiveUnit)

        logger.info("ExplosiveUnit with name ${createExplosiveUnitRequest.name} created")
        return savedExplosiveUnit.id!!
    }


    @AdminAuthorization
    fun deleteExplosiveUnit(id: Long) {
        logger.info("Deleting ExplosiveUnit with id $id")
        explosiveUnitService.delete(id)
        logger.info("ExplosiveUnit with $id deleted")
    }

    fun getExplosivesMaterialsById(ids: Array<String>): List<ExplosiveUnitDto> {
        val explosives = mutableListOf<ExplosiveUnitDto>()
        ids.forEach { id ->
            val explosive = getExplosiveUnitById(id.toLong())
            explosives.add(explosive)
        }
        return explosives
    }

    private fun mapExplosiveUnitToExplosiveUnitDto(explosiveUnit: ExplosiveUnit) : ExplosiveUnitDto {
        val explosiveMaterialsQuantity = mutableListOf<ExplosiveMaterialQuantityDto>()
        explosiveUnit.explosiveMaterials.forEach {
            val item = ExplosiveMaterialQuantityDto(explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(it.explosiveMaterial), quantity = it.quantity)
            explosiveMaterialsQuantity.add(item)
        }

        return ExplosiveUnitDto(
            id = explosiveUnit.id,
            name = explosiveUnit.name,
            description = explosiveUnit.description,
            newTnt = explosiveUnit.newTnt,
            newActual = explosiveUnit.newActual,
            thumbnail = storageService.downloadImage(ObjectType.EXPLOSIVE_UNITS.bucket, explosiveUnit.photos.first().thumbnailPath),
            makeTime = explosiveUnit.makeTime,
            msd = explosiveUnit.msd,
            explosiveUnitType = explosiveUnit.explosiveUnitType,
            creationDate = explosiveUnit.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            updateDate = explosiveUnit.updateDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            createdBy = explosiveUnit.createdBy,
            updatedBy = explosiveUnit.updatedBy,
            explosiveMaterials = explosiveMaterialsQuantity
        )
    }

    private fun prepareFilterQuery(filter: ExplosiveUnitFilterDTO): BooleanExpression {
        val qExplosiveUnit: QExplosiveUnit = QExplosiveUnit.explosiveUnit

        return qExplosiveUnit.id.isNotNull
            .and(qExplosiveUnit.name.containsIgnoreCaseOrNull(filter.q))
            .and(qExplosiveUnit.explosiveUnitType.eqOrNull(filter.explosiveUnitType))
    }
}