package com.htt.bis.facade.database

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.Photo
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.QBuildMaterial
import com.htt.bis.dto.database.build_material.CreateBuildMaterialRequest
import com.htt.bis.dto.database.build_material.BuildMaterialDto
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.build_material.UpdateBuildMaterialRequest
import com.htt.bis.exception.DomainObjectAlreadyApprovedException
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.mapper.BuildMaterialMapper
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.BuildMaterialService
import com.htt.bis.service.storage.PhotoService
import com.htt.bis.service.storage.StorageService
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class BuildMaterialFacade(
    private val buildMaterialService: BuildMaterialService,
    private val buildMaterialMapper: BuildMaterialMapper,
    private val storageService: StorageService,
    private val authenticationService: AuthenticationService,
    private val photoService : PhotoService
) {
    var logger: Logger = LoggerFactory.getLogger(BuildMaterialFacade::class.java)

    @UserAuthorization
    fun getAllBuildMaterials(filter : EntityFilter, pageNo: Int, pageSize: Int, sort: List<String>?) : Page<BuildMaterialDto> {
        logger.info("Getting page $pageNo from BuildMaterials")
        val query = prepareFilterQuery(filter)
        return buildMaterialService.getAll(query, pageNo, pageSize, sort).map{
            buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(it)
        }
    }

    @UserAuthorization
    fun getBuildMaterialsById(ids : Array<String>) : List<BuildMaterialDto> {
        val materials = mutableListOf<BuildMaterialDto>()
        ids.forEach { id ->
            var material = getBuildMaterialById(id.toLong())
            materials.add(material)
        }
        return materials
    }

    @UserAuthorization
    fun getBuildMaterialById(id : Long) : BuildMaterialDto {
        logger.info("Getting BuildMaterials with id $id")
        buildMaterialService.getById(id).ifFound {
            return buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(it)
        }
    }

    @UserAuthorization
    fun getBuildMaterialThumbnail(id : Long) : String {
        buildMaterialService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.BUILD_MATERIALS.bucket, it.photo!!.thumbnailPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun getBuildMaterialPhoto(id : Long) : String {
        buildMaterialService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.BUILD_MATERIALS.bucket, it.photo!!.originalPath!!)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @AdminAuthorization
    fun approveBuildMaterialById(id : Long) : BuildMaterialDto {
        logger.info("Approving BuildMaterials with id $id")
        buildMaterialService.getById(id).ifFound {
            if (it.isApproved){
                throw DomainObjectAlreadyApprovedException()
            }
            return buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(buildMaterialService.update(it.copy(isApproved = true, updateDate = LocalDateTime.now())))
        }
    }

    @AdminAuthorization
    fun updateBuildMaterial(id : Long, updateBuildMaterialRequest: UpdateBuildMaterialRequest) : BuildMaterialDto {
        logger.info("Updating BuildMaterial with name ${updateBuildMaterialRequest.name}")
        buildMaterialService.getById(id).ifFound {
            val userId = authenticationService.getCurrentLoggedUserId()
            val buildMaterialToUpdate = it.copy(id = it.id, name = updateBuildMaterialRequest.name!!, aFactor = updateBuildMaterialRequest.aFactor!!,
             isApproved = updateBuildMaterialRequest.isApproved!!, updateDate = LocalDateTime.now(),updatedBy = userId.toString())

           /* if(updateBuildMaterialRequest.photoBase64!=null){
                storageService.uploadDatabaseImage(ObjectType.EXPLOSIVE_MATERIAL.bucket,updateBuildMaterialRequest.photoBase64, id.toString())
            }*/
            return buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(buildMaterialService.update(buildMaterialToUpdate))
        }
    }

    @UserAuthorization
    fun createBuildMaterial(createBuildMaterialRequest: CreateBuildMaterialRequest) : BuildMaterialDto {
        logger.info("Creating BuildMaterial with name ${createBuildMaterialRequest.name}")
        val isApproved = authenticationService.isCurrentUserAdmin()
        val userId = authenticationService.getCurrentLoggedUserId()
        val buildMaterialToSave = BuildMaterial(name = createBuildMaterialRequest.name, aFactor = createBuildMaterialRequest.aFactor,
             isApproved = isApproved, creationDate = LocalDateTime.now(), updateDate = LocalDateTime.now(), createdBy = userId.toString())
        val savedBuildMaterial = buildMaterialService.add(buildMaterialToSave)
        photoService.addPhoto(
            Photo(originalPath = "${savedBuildMaterial.id}/ORIGINAL.jpeg", thumbnailPath = "${savedBuildMaterial.id}/THUMBNAIL.jpeg",
            name = "BuildMaterialPhoto-${savedBuildMaterial.id}",type="jpeg",uploadedBy = userId.toString(),uploadTime = LocalDateTime.now(), buildMaterial = savedBuildMaterial), createBuildMaterialRequest.photos!![0], ObjectType.BUILD_MATERIALS
        )
        logger.info("BuildMaterial with name ${createBuildMaterialRequest.name} created")
        return buildMaterialMapper.buildMaterialObjectToBuildMaterialDTO(savedBuildMaterial)
    }


    @AdminAuthorization
    fun deleteBuildMaterial(id : Long) {
        logger.info("Deleting BuildMaterial with id $id")
        buildMaterialService.delete(id)
        logger.info("BuildMaterial with $id deleted")
    }

    private fun prepareFilterQuery(filter: EntityFilter): BooleanExpression {
        val qBuildMaterial: QBuildMaterial = QBuildMaterial.buildMaterial

        return qBuildMaterial.id.isNotNull
            .and(qBuildMaterial.name.containsIgnoreCaseOrNull(filter.q))
    }
}