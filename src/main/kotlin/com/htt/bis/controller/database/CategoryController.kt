package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.simple.CategoryDto
import com.htt.bis.dto.database.simple.CreateCategoryRequest
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.database.CategoryFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/categories")
@Api(tags = ["Category endpoint"])
internal class CategoryController(
    private val categoryFacade: CategoryFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(EntityFilter::class.java, EntityFilterEditor())
    }

    @ApiOperation(value = "Create new Category", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Category created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewCategory(@Valid @RequestBody createCategoryRequest: CreateCategoryRequest): ResponseEntity<CategoryDto> {
        return ResponseEntity(categoryFacade.createCategory(createCategoryRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Category image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Category created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( categoryFacade.getCategoryPhoto(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Get Category thumbnail", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Category thumbnail returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/thumbnail"])
    @CrossOrigin
    fun downloadThumbnail(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( categoryFacade.getCategoryThumbnail(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all Category", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Category returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllCategories(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: EntityFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(categoryFacade.getAllCategories(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(categoryFacade.getCategoriesById(filter!!.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get Category by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Category returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getCategoryById(
        @PathVariable id: Long,
    ): ResponseEntity<CategoryDto> {
        return ResponseEntity(categoryFacade.getCategoryById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete category by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Category deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(categoryFacade.deleteCategory(id), HttpStatus.OK)
    }
}