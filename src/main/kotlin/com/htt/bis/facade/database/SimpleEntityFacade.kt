package com.htt.bis.facade.database

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.common.eqOrNull
import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.Photo
import com.htt.bis.domain.core.SimpleEntity
import com.htt.bis.domain.core.QSimpleEntity
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.SimpleEntityFilter
import com.htt.bis.dto.database.simple.SimpleEntityDto
import com.htt.bis.dto.database.simple.CreateSimpleEntityRequest
import com.htt.bis.exception.SimpleEntityAlreadyExistException
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.CategoryService
import com.htt.bis.service.database.SimpleEntityService
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
class SimpleEntityFacade(
    private val categoryService: CategoryService,
    private val authenticationService: AuthenticationService,
    private val storageService: StorageService,
    private val simpleEntityService: SimpleEntityService,
    private val photoService: PhotoService
) {
    var logger: Logger = LoggerFactory.getLogger(SimpleEntityFacade::class.java)

    @UserAuthorization
    fun getAllSimpleEntities(filter : SimpleEntityFilter, pageNo: Int, pageSize: Int, sort: List<String>?) : Page<SimpleEntityDto> {
        logger.info("Getting page $pageNo from SimpleEntities")
        val query = prepareFilterQuery(filter)
        return simpleEntityService.getAll(query, pageNo, pageSize, sort).map{
            mapSimpleEntityToSimpleEntityDto(it)
        }
    }

    @UserAuthorization
    fun getSimpleEntitiesById(ids : Array<String>) : List<SimpleEntityDto> {
        val categories = mutableListOf<SimpleEntityDto>()
        ids.forEach { id ->
            val simpleEntity = getSimpleEntityById(id.toLong())
            categories.add(simpleEntity)
        }
        return categories
    }

    @UserAuthorization
    fun getSimpleEntityById(id : Long) : SimpleEntityDto {
        logger.info("Getting SimpleEntities with id $id")
        simpleEntityService.getById(id).ifFound {
            return mapSimpleEntityToSimpleEntityDto(it)
        }
    }

    @AdminAuthorization
    fun createSimpleEntity(createSimpleEntityRequest: CreateSimpleEntityRequest) : SimpleEntityDto {
        logger.info("Creating SimpleEntity with name ${createSimpleEntityRequest.name}")
        val userId = authenticationService.getCurrentLoggedUserId()
        categoryService.getById(createSimpleEntityRequest.categoryId).ifFound {
            if(simpleEntityService.getByCategoryAndName(it, createSimpleEntityRequest.name)!=null){
                throw SimpleEntityAlreadyExistException()
            }
            val simpleEntityToSave = SimpleEntity(name = createSimpleEntityRequest.name,  creationDate = LocalDateTime.now(), createdBy = userId.toString(), unitType = createSimpleEntityRequest.unitType, category = it)
            val savedSimpleEntity = simpleEntityService.add(simpleEntityToSave)

            photoService.addPhoto(
                Photo(originalPath = "${savedSimpleEntity.id}/ORIGINAL.jpeg", thumbnailPath = "${savedSimpleEntity.id}/THUMBNAIL.jpeg",
                    name = "${it.name.uppercase()}-${savedSimpleEntity.id}",type="jpeg",uploadedBy = userId.toString(),uploadTime = LocalDateTime.now(), simpleEntity = savedSimpleEntity), createSimpleEntityRequest.photos[0], ObjectType.SIMPLE_ENTITY
            )
            logger.info("SimpleEntity with name ${createSimpleEntityRequest.name} created")
            return SimpleEntityDto(name = savedSimpleEntity.name, id = savedSimpleEntity.id)
        }

    }
    @UserAuthorization
    fun getSimpleEntityPhoto(id : Long) : String {
        simpleEntityService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.SIMPLE_ENTITY.bucket, it.photo!!.originalPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun getSimpleEntityThumbnail(id : Long) : String {
        simpleEntityService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.SIMPLE_ENTITY.bucket, it.photo!!.thumbnailPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @AdminAuthorization
    fun deleteSimpleEntity(id : Long) {
        logger.info("Deleting SimpleEntity with id $id")
        simpleEntityService.delete(id)
        logger.info("SimpleEntity with $id deleted")
    }

    private fun prepareFilterQuery(filter: SimpleEntityFilter): BooleanExpression {
        val qSimpleEntity: QSimpleEntity = QSimpleEntity.simpleEntity

        return qSimpleEntity.id.isNotNull
            .and(qSimpleEntity.name.containsIgnoreCaseOrNull(filter.q))
            .and(qSimpleEntity.category.name.eqOrNull(filter.categoryName))
            .and(qSimpleEntity.category.id.eqOrNull(filter.categoryId))
    }

    private fun mapSimpleEntityToSimpleEntityDto(simpleEntity: SimpleEntity): SimpleEntityDto {

        return SimpleEntityDto(
            id = simpleEntity.id,
            categoryId = simpleEntity.category.id,
            name = simpleEntity.name,
            image = getSimpleEntityPhoto(simpleEntity.id!!),
            unitType = simpleEntity.unitType.name,
            creationDate = simpleEntity.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            createdBy = simpleEntity.createdBy
        )
    }
}