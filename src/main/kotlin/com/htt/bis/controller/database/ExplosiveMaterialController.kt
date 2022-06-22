package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.explosive_material.CreateExplosiveMaterialRequest
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.explosive_material.ExplosiveMaterialDto
import com.htt.bis.dto.database.explosive_material.UpdateExplosiveMaterialRequest
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.database.ExplosiveMaterialFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/explosive-material")
@Api(tags = ["Explosive material endpoint"])
internal class ExplosiveMaterialController(
    private val explosiveMaterialFacade: ExplosiveMaterialFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(EntityFilter::class.java, EntityFilterEditor())
    }

    @ApiOperation(value = "Create new Explosive Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Explosive material created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewExplosiveMaterial(@Valid @RequestBody createExplosiveMaterialRequest: CreateExplosiveMaterialRequest): ResponseEntity<ExplosiveMaterialDto> {
        return ResponseEntity(explosiveMaterialFacade.createExplosiveMaterial(createExplosiveMaterialRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Explosive Material image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Explosive material created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( explosiveMaterialFacade.getExplosiveMaterialPhoto(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Get Explosive Material thumbnail", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Explosive material thumbnail returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/thumbnail"])
    @CrossOrigin
    fun downloadThumbnail(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( explosiveMaterialFacade.getExplosiveMaterialThumbnail(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all Explosive Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive material returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllExplosiveMaterials(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: EntityFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(explosiveMaterialFacade.getAllExplosivesMaterials(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(explosiveMaterialFacade.getExplosivesMaterialsById(filter!!.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get Explosive Material by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive material returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getExplosiveMaterialById(
        @PathVariable id: Long,
    ): ResponseEntity<ExplosiveMaterialDto> {
        return ResponseEntity(explosiveMaterialFacade.getExplosiveMaterialById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Update Explosive Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive material updated."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}")
    fun updateExplosiveMaterial(@PathVariable id : Long, @Valid @RequestBody updateExplosiveMaterialRequest: UpdateExplosiveMaterialRequest): ResponseEntity<ExplosiveMaterialDto> {
        return ResponseEntity(explosiveMaterialFacade.updateExplosiveMaterial(id, updateExplosiveMaterialRequest), HttpStatus.OK)
    }

    @ApiOperation(value = "Approve Explosive Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive material approved."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}/approve")
    fun approveExplosiveMaterial(@PathVariable id : Long): ResponseEntity<ExplosiveMaterialDto> {
        return ResponseEntity(explosiveMaterialFacade.approveExplosiveMaterialById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete explosive material by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosice material deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteExplosiveMaterial(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(explosiveMaterialFacade.deleteExplosiveMaterial(id), HttpStatus.OK)
    }
}