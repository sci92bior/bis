package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.exercise.*
import com.htt.bis.editors.ExerciseFilterEditor
import com.htt.bis.facade.exercise.ExerciseFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/exercise")
@Api(tags = ["Exercise endpoint"])
internal class ExerciseController(
    private val exerciseFacade: ExerciseFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(ExerciseFilterDTO::class.java, ExerciseFilterEditor())
    }

    @ApiOperation(value = "Create new Exercise", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Exercise created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewExercise(@Valid @RequestBody createExerciseRequest: CreateExerciseRequest): ResponseEntity<Long> {
        return ResponseEntity(exerciseFacade.createExercise(createExerciseRequest), HttpStatus.CREATED)
    }


    @ApiOperation(value = "Get all Exercises", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Exercise returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllExercises(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: ExerciseFilterDTO
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(exerciseFacade.getAllExercises(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(exerciseFacade.getExercisesById(filter.ids!!), HttpStatus.OK)
        }

    }



    @ApiOperation(value = "Get Exercise by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Exercise returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getExerciseById(
        @PathVariable id: Long,
    ): ResponseEntity<ExerciseDTO> {
        return ResponseEntity(exerciseFacade.getExerciseById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete exercise by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Exercise deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteExercise(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(exerciseFacade.deleteExercise(id), HttpStatus.OK)
    }
}