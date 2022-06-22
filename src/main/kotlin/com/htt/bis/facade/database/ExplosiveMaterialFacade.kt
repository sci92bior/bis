package com.htt.bis.facade.database

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.Photo
import com.htt.bis.domain.QExplosiveMaterial
import com.htt.bis.dto.database.explosive_material.CreateExplosiveMaterialRequest
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.explosive_material.ExplosiveMaterialDto
import com.htt.bis.dto.database.explosive_material.UpdateExplosiveMaterialRequest
import com.htt.bis.exception.DomainObjectAlreadyApprovedException
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.mapper.ExplosiveMaterialMapper
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.ExplosiveMaterialService
import com.htt.bis.service.storage.PhotoService
import com.htt.bis.service.storage.StorageService
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class ExplosiveMaterialFacade(
    private val explosiveMaterialService: ExplosiveMaterialService,
    private val explosiveMaterialMapper: ExplosiveMaterialMapper,
    private val storageService: StorageService,
    private val photoService: PhotoService,
    private val authenticationService: AuthenticationService
) {
    var logger: Logger = LoggerFactory.getLogger(ExplosiveMaterialFacade::class.java)

    @UserAuthorization
    fun getAllExplosivesMaterials(filter : EntityFilter, pageNo: Int, pageSize: Int, sort: List<String>?) : Page<ExplosiveMaterialDto> {
        logger.info("Getting page $pageNo from ExplosiveMaterials")
        val query = prepareFilterQuery(filter)
        return explosiveMaterialService.getAll(query, pageNo, pageSize, sort).map{
            explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(it)
        }
    }

    @UserAuthorization
    fun getExplosiveMaterialById(id : Long) : ExplosiveMaterialDto {
        logger.info("Getting ExplosiveMaterials with id $id")
        explosiveMaterialService.getById(id).ifFound {
            return explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(it)
        }
    }

    @UserAuthorization
    fun getExplosiveMaterialPhoto(id : Long) : String {
        explosiveMaterialService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.EXPLOSIVE_MATERIAL.bucket, it.photo!!.originalPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun getExplosiveMaterialThumbnail(id : Long) : String {
        explosiveMaterialService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.EXPLOSIVE_MATERIAL.bucket, it.photo!!.thumbnailPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @AdminAuthorization
    fun approveExplosiveMaterialById(id : Long) : ExplosiveMaterialDto {
        logger.info("Approving ExplosiveMaterials with id $id")
        explosiveMaterialService.getById(id).ifFound {
            if (it.isApproved){
                throw DomainObjectAlreadyApprovedException()
            }
            return explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(explosiveMaterialService.update(it.copy(isApproved = true, updateDate = LocalDateTime.now())))
        }
    }

    @UserAuthorization
    fun updateExplosiveMaterial(id : Long, updateExplosiveMaterialRequest: UpdateExplosiveMaterialRequest) : ExplosiveMaterialDto {
        logger.info("Updating ExplosiveMaterial with name ${updateExplosiveMaterialRequest.name}")
        explosiveMaterialService.getById(id).ifFound {
            val userId = authenticationService.getCurrentLoggedUserId()
            val explosiveMaterialToUpdate = it.copy(id = it.id, name = updateExplosiveMaterialRequest.name!!, grain = updateExplosiveMaterialRequest.grain!!,
            reFactor = updateExplosiveMaterialRequest.reFactor!!, unitType = updateExplosiveMaterialRequest.unitType!!, isApproved = updateExplosiveMaterialRequest.isApproved!! ,updateDate = LocalDateTime.now(), updatedBy = userId.toString())

            return explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(explosiveMaterialService.update(explosiveMaterialToUpdate))
        }
    }

    @UserAuthorization
    fun createExplosiveMaterial(createExplosiveMaterialRequest: CreateExplosiveMaterialRequest) : ExplosiveMaterialDto {
        logger.info("Creating ExplosiveMaterial with name ${createExplosiveMaterialRequest.name}")
        val isApproved = authenticationService.isCurrentUserAdmin()
        val currentUser = authenticationService.getCurrentLoggedUserId()
        val explsiveMaterialToSave = ExplosiveMaterial(name = createExplosiveMaterialRequest.name, grain = createExplosiveMaterialRequest.grain,
            unitType = createExplosiveMaterialRequest.unitType, reFactor = createExplosiveMaterialRequest.rFactor, isApproved = isApproved, creationDate = LocalDateTime.now(), updateDate = LocalDateTime.now(), createdBy = currentUser.toString())
        val savedExplosiveMaterial = explosiveMaterialService.add(explsiveMaterialToSave)
        photoService.addPhoto(Photo(originalPath = "${savedExplosiveMaterial.id}/ORIGINAL.jpeg", thumbnailPath = "${savedExplosiveMaterial.id}/THUMBNAIL.jpeg",
            name = "ExplosiveMaterialPhoto-${savedExplosiveMaterial.id}",type="jpeg",uploadedBy = currentUser.toString(),uploadTime = LocalDateTime.now(), explosiveMaterial = savedExplosiveMaterial), createExplosiveMaterialRequest.photos!![0], ObjectType.EXPLOSIVE_MATERIAL
        )
        logger.info("ExplosiveMaterial with name ${createExplosiveMaterialRequest.name} created")
        return explosiveMaterialMapper.explosiveMaterialObjectToExplosiveMaterialDTO(explosiveMaterialService.getById(savedExplosiveMaterial.id!!)!!)
    }


    @AdminAuthorization
    fun deleteExplosiveMaterial(id : Long) {
        logger.info("Deleting ExplosiveMaterial with id $id")
        explosiveMaterialService.delete(id)
        logger.info("ExplosiveMaterial with $id deleted")
    }

    fun getExplosivesMaterialsById(ids: Array<String>): List<ExplosiveMaterialDto> {
        val explosives = mutableListOf<ExplosiveMaterialDto>()
        ids.forEach { id ->
            val explosive = getExplosiveMaterialById(id.toLong())
            explosives.add(explosive)
        }
        return explosives
    }

    private fun prepareFilterQuery(filter: EntityFilter): BooleanExpression {
        val qExplosiveMaterial: QExplosiveMaterial = QExplosiveMaterial.explosiveMaterial

        return qExplosiveMaterial.id.isNotNull
            .and(qExplosiveMaterial.name.containsIgnoreCaseOrNull(filter.q))
    }
}