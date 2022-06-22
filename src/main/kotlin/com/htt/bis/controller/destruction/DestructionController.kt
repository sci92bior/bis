package com.htt.bis.controller.destruction

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.destruction.CreateDestructionRequest
import com.htt.bis.dto.destruction.DestructionDto
import com.htt.bis.dto.destruction.DestructionFilterDTO
import com.htt.bis.editors.DestructionFilterEditor
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.destruction.DestructionFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/destruction")
@Api(tags = ["Destruction endpoint"])
internal class DestructionController(
    private val destructionFacade: DestructionFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(DestructionFilterDTO::class.java, DestructionFilterEditor())
    }

    @ApiOperation(value = "Create new Destruction", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Destruction created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewDestruction(@Valid @RequestBody createDestructionRequest: CreateDestructionRequest): ResponseEntity<Long> {
        return ResponseEntity(destructionFacade.createDestruction(createDestructionRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Destruction images", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Destruction Images returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long): ResponseEntity<Map<String,List<PhotoDto>>>? {
        return ResponseEntity( destructionFacade.getDestructionPhotos(id), HttpStatus.OK)
    }


    /*@ApiOperation(value = "Get Destruction image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Destruction created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<List<PhotoDto>>? {
        return ResponseEntity( destructionFacade.getDestructionPhotos(id), HttpStatus.OK)
    }*/


    @ApiOperation(value = "Get all Destruction", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Destruction returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllDestructions(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam filter: DestructionFilterDTO
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(destructionFacade.getAllDestructions(filter,pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(destructionFacade.getDestructionsById(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get Destruction by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Destruction returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getDestructionById(
        @PathVariable id: Long,
    ): ResponseEntity<DestructionDto> {
        return ResponseEntity(destructionFacade.getDestructionById(id), HttpStatus.OK)
    }

   /* @ApiOperation(value = "Update Destruction", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Destruction updated."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}")
    fun updateDestruction(@PathVariable id : Long, @Valid @RequestBody updateDestructionRequest: UpdateDestructionRequest): ResponseEntity<DestructionDto> {
        return ResponseEntity(destructionFacade.updateDestruction(id, updateDestructionRequest), HttpStatus.OK)
    }*/


    @ApiOperation(value = "Delete destruction by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Destruction deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteDestruction(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(destructionFacade.deleteDestruction(id), HttpStatus.OK)
    }
}