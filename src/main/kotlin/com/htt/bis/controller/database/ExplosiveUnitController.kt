package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.explosive_unit.CreateExplosiveUnitRequest
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitDto
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitFilterDTO
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.editors.ExplosiveUnitFilterEditor
import com.htt.bis.facade.explosive_unit.ExplosiveUnitFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/explosive-unit")
@Api(tags = ["Explosive unit endpoint"])
internal class ExplosiveUnitController(
    private val explosiveUnitFacade: ExplosiveUnitFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(ExplosiveUnitFilterDTO::class.java, ExplosiveUnitFilterEditor())
    }

    @ApiOperation(value = "Create new Explosive Unit", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Explosive unit created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewExplosiveUnit(@Valid @RequestBody createExplosiveUnitRequest: CreateExplosiveUnitRequest): ResponseEntity<Long> {
        return ResponseEntity(explosiveUnitFacade.createExplosiveUnit(createExplosiveUnitRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Explosive Unit image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Explosive unit created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<List<PhotoDto>>? {
        return ResponseEntity( explosiveUnitFacade.getExplosiveUnitPhotos(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all Explosive Unit", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive unit returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllExplosiveUnits(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: ExplosiveUnitFilterDTO
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(explosiveUnitFacade.getAllExplosivesUnits(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(explosiveUnitFacade.getExplosivesMaterialsById(filter!!.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get Explosive Unit by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive unit returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getExplosiveUnitById(
        @PathVariable id: Long,
    ): ResponseEntity<ExplosiveUnitDto> {
        return ResponseEntity(explosiveUnitFacade.getExplosiveUnitById(id), HttpStatus.OK)
    }

   /* @ApiOperation(value = "Update Explosive Unit", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive unit updated."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}")
    fun updateExplosiveUnit(@PathVariable id : Long, @Valid @RequestBody updateExplosiveUnitRequest: UpdateExplosiveUnitRequest): ResponseEntity<ExplosiveUnitDto> {
        return ResponseEntity(explosiveUnitFacade.updateExplosiveUnit(id, updateExplosiveUnitRequest), HttpStatus.OK)
    }*/


    @ApiOperation(value = "Delete explosive unit by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Explosive unit deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteExplosiveUnit(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(explosiveUnitFacade.deleteExplosiveUnit(id), HttpStatus.OK)
    }
}