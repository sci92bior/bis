package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.SimpleEntityFilter
import com.htt.bis.dto.database.simple.SimpleEntityDto
import com.htt.bis.dto.database.simple.CreateSimpleEntityRequest
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.editors.SimpleEntityFilterEditor
import com.htt.bis.facade.database.SimpleEntityFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/simple-entity")
@Api(tags = ["SimpleEntity endpoint"])
internal class SimpleEntityController(
    private val simpleEntityFacade: SimpleEntityFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(SimpleEntityFilter::class.java, SimpleEntityFilterEditor())
    }

    @ApiOperation(value = "Create new SimpleEntity", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "SimpleEntity created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewSimpleEntity(@Valid @RequestBody createSimpleEntityRequest: CreateSimpleEntityRequest): ResponseEntity<SimpleEntityDto> {
        return ResponseEntity(simpleEntityFacade.createSimpleEntity(createSimpleEntityRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get SimpleEntity image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "SimpleEntity created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( simpleEntityFacade.getSimpleEntityPhoto(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Get SimpleEntity thumbnail", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "SimpleEntity thumbnail returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/thumbnail"])
    @CrossOrigin
    fun downloadThumbnail(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<String>? {
        return ResponseEntity( simpleEntityFacade.getSimpleEntityThumbnail(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all SimpleEntity", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "SimpleEntity returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllSimpleEntities(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: SimpleEntityFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(simpleEntityFacade.getAllSimpleEntities(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(simpleEntityFacade.getSimpleEntitiesById(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get SimpleEntity by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "SimpleEntity returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getSimpleEntityById(
        @PathVariable id: Long,
    ): ResponseEntity<SimpleEntityDto> {
        return ResponseEntity(simpleEntityFacade.getSimpleEntityById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete simpleEntity by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "SimpleEntity deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteSimpleEntity(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(simpleEntityFacade.deleteSimpleEntity(id), HttpStatus.OK)
    }
}