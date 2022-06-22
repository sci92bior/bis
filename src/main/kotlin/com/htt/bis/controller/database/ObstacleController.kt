package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.database.obstacle.CreateObstacleRequest
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.database.obstacle.ObstacleDto
import com.htt.bis.dto.database.PhotoDto
import com.htt.bis.dto.database.obstacle.ObstacleFilterDTO
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.editors.ObstacleFilterEditor
import com.htt.bis.facade.obstacle.ObstacleFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/obstacle")
@Api(tags = ["Obstacle endpoint"])
internal class ObstacleController(
    private val obstacleFacade: ObstacleFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(ObstacleFilterDTO::class.java, ObstacleFilterEditor())
    }

    @ApiOperation(value = "Create new Obstacle", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Obstacle created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewObstacle(@Valid @RequestBody createObstacleRequest: CreateObstacleRequest): ResponseEntity<Long> {
        return ResponseEntity(obstacleFacade.createObstacle(createObstacleRequest), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Get Obstacle image", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Obstacle created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping(value = ["/{id}/image"])
    @CrossOrigin
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<List<PhotoDto>>? {
        return ResponseEntity( obstacleFacade.getObstaclePhotos(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get all Obstacle", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Obstacle returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllObstacles(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: ObstacleFilterDTO
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(obstacleFacade.getAllObstacles(filter,pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(obstacleFacade.getExplosivesMaterialsById(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get Obstacle by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Obstacle returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getObstacleById(
        @PathVariable id: Long,
    ): ResponseEntity<ObstacleDto> {
        return ResponseEntity(obstacleFacade.getObstacleById(id), HttpStatus.OK)
    }

   /* @ApiOperation(value = "Update Obstacle", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Obstacle updated."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/{id}")
    fun updateObstacle(@PathVariable id : Long, @Valid @RequestBody updateObstacleRequest: UpdateObstacleRequest): ResponseEntity<ObstacleDto> {
        return ResponseEntity(obstacleFacade.updateObstacle(id, updateObstacleRequest), HttpStatus.OK)
    }*/


    @ApiOperation(value = "Delete obstacle by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Obstacle deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteObstacle(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(obstacleFacade.deleteObstacle(id), HttpStatus.OK)
    }
}