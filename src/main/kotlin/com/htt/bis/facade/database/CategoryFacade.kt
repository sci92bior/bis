package com.htt.bis.facade.database

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.domain.ObjectType
import com.htt.bis.domain.Photo
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.core.QCategory
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.simple.CategoryDto
import com.htt.bis.dto.database.simple.CreateCategoryRequest
import com.htt.bis.exception.CategoryAlreadyExistException
import com.htt.bis.exception.ImageNotFoundException
import com.htt.bis.facade.ifFound
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.CategoryService
import com.htt.bis.service.storage.PhotoService
import com.htt.bis.service.storage.StorageService
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class CategoryFacade(
    private val categoryService: CategoryService,
    private val authenticationService: AuthenticationService,
    private val storageService: StorageService,
    private val photoService: PhotoService
) {
    var logger: Logger = LoggerFactory.getLogger(CategoryFacade::class.java)

    @UserAuthorization
    fun getAllCategories(filter : EntityFilter, pageNo: Int, pageSize: Int, sort: List<String>?) : Page<CategoryDto> {
        logger.info("Getting page $pageNo from Categories")
        val query = prepareFilterQuery(filter)
        return categoryService.getAll(query, pageNo, pageSize, sort).map{
            mapCategoryToCategoryDto(it)
        }
    }

    @UserAuthorization
    fun getCategoriesById(ids : Array<String>) : List<CategoryDto> {
        val categories = mutableListOf<CategoryDto>()
        ids.forEach { id ->
            val category = getCategoryById(id.toLong())
            categories.add(category)
        }
        return categories
    }

    @UserAuthorization
    fun getCategoryById(id : Long) : CategoryDto {
        logger.info("Getting Categories with id $id")
        categoryService.getById(id).ifFound {
            return mapCategoryToCategoryDto(it)
        }
    }

    @AdminAuthorization
    fun createCategory(createCategoryRequest: CreateCategoryRequest) : CategoryDto {
        logger.info("Creating Category with name ${createCategoryRequest.name}")
        val userId = authenticationService.getCurrentLoggedUserId()
        if(categoryService.getByName(createCategoryRequest.name)!=null){
            throw CategoryAlreadyExistException()
        }
        val categoryToSave = Category(name = createCategoryRequest.name,  creationDate = LocalDateTime.now(), createdBy = userId.toString())
        val savedCategory = categoryService.add(categoryToSave)

        photoService.addPhoto(
            Photo(originalPath = "${savedCategory.id}/ORIGINAL.jpeg", thumbnailPath = "${savedCategory.id}/THUMBNAIL.jpeg",
                name = "BuildMaterialPhoto-${savedCategory.id}",type="jpeg",uploadedBy = userId.toString(),uploadTime = LocalDateTime.now(), category = savedCategory), createCategoryRequest.photos!![0], ObjectType.CATEGORY
        )
        logger.info("Category with name ${createCategoryRequest.name} created")
        return CategoryDto(name = savedCategory.name, id = savedCategory.id)
    }
    @UserAuthorization
    fun getCategoryPhoto(id : Long) : String {
        categoryService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.CATEGORY.bucket, it.photo!!.originalPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @UserAuthorization
    fun getCategoryThumbnail(id : Long) : String {
        categoryService.getById(id).ifFound {
            if(it.photo!=null) {
                return storageService.downloadImage(ObjectType.CATEGORY.bucket, it.photo!!.thumbnailPath)
            }else{
                throw ImageNotFoundException()
            }
        }
    }

    @AdminAuthorization
    fun deleteCategory(id : Long) {
        logger.info("Deleting Category with id $id")
        categoryService.delete(id)
        logger.info("Category with $id deleted")
    }

    private fun prepareFilterQuery(filter: EntityFilter): BooleanExpression {
        val qCategory: QCategory = QCategory.category

        return qCategory.id.isNotNull
            .and(qCategory.name.containsIgnoreCaseOrNull(filter.q))
    }

    private fun mapCategoryToCategoryDto(category: Category): CategoryDto {

        return CategoryDto(
            id = category.id,
            name = category.name,
            image = getCategoryPhoto(category.id!!)
        )
    }
}