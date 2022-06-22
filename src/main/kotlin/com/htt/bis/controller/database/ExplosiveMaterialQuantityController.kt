package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.explosive_unit.ExplosiveMaterialQuantityFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/explosive-material-quantity")
@Api(tags = ["Explosive material endpoint"])
internal class ExplosiveMaterialQuantityController(
    private val explosiveMaterialQuantityFacade: ExplosiveMaterialQuantityFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(EntityFilter::class.java, EntityFilterEditor())
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
        @RequestParam(required = false) filter: EntityFilter?
    ): ResponseEntity<Any> {
            return ResponseEntity(explosiveMaterialQuantityFacade.getExplosivesMaterialsQuantityByExplosiveUnitIds(filter!!.ids!!), HttpStatus.OK)
    }


}