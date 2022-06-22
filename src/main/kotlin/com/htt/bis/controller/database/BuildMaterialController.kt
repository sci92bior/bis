package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.build_material.CreateBuildMaterialRequest
import com.htt.bis.dto.database.build_material.BuildMaterialDto
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.build_material.UpdateBuildMaterialRequest
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.database.BuildMaterialFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/build-material")
@Api(tags = ["Build material endpoint"])
internal class BuildMaterialController(
    private val buildMaterialFacade: BuildMaterialFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(EntityFilter::class.java, EntityFilterEditor())
    }


    @ApiOperation(value = "Create new Build Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Build material created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewBuildMaterial(@Valid @RequestBody createBuildMaterialRequest: CreateBuildMaterialRequest): ResponseEntity<BuildMaterialDto> {
        return ResponseEntity(buildMaterialFacade.createBuildMaterial(createBuildMaterialRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Build Material image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Build material created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( buildMaterialFacade.getBuildMaterialPhoto(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Get Build Material thumbnail", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Build material created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/thumbnail"])
    @CrossOrigin
    fun downloadThumbnail(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( buildMaterialFacade.getBuildMaterialThumbnail(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all Build Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Build material returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllBuildMaterials(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "filter", required = false) filter : EntityFilter,
        @RequestParam(value = "sort", required = false) sort: List<String>?
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(buildMaterialFacade.getAllBuildMaterials(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(buildMaterialFacade.getBuildMaterialsById(filter!!.ids!!), HttpStatus.OK)
        }
    }

    @ApiOperation(value = "Get Build Material by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Build material returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getBuildMaterialById(
        @PathVariable id: Long,
    ): ResponseEntity<BuildMaterialDto> {
        return ResponseEntity(buildMaterialFacade.getBuildMaterialById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Update Build Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Build material updated."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}")
    fun updateBuildMaterial(@PathVariable id : Long, @Valid @RequestBody updateBuildMaterialRequest: UpdateBuildMaterialRequest): ResponseEntity<BuildMaterialDto> {
        return ResponseEntity(buildMaterialFacade.updateBuildMaterial(id, updateBuildMaterialRequest), HttpStatus.OK)
    }

    @ApiOperation(value = "Approve Build Material", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Build material approved."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}/approve")
    fun approveBuildMaterial(@PathVariable id : Long): ResponseEntity<BuildMaterialDto> {
        return ResponseEntity(buildMaterialFacade.approveBuildMaterialById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete build material by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Build material deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteBuildMaterial(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(buildMaterialFacade.deleteBuildMaterial(id), HttpStatus.OK)
    }
}