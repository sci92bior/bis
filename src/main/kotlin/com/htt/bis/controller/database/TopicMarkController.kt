package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.course.TopicMarkDto
import com.htt.bis.dto.course.TopicMarkFilter
import com.htt.bis.editors.TopicMarkFilterEditor
import com.htt.bis.facade.course.TopicMarkFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/topic-mark")
@Api(tags = ["Topic endpoint"])
internal class TopicMarkController(
    private val topicMarkFacade: TopicMarkFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(TopicMarkFilter::class.java, TopicMarkFilterEditor())
    }

    @ApiOperation(value = "Get all Topic Marks", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic marks returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllTopicMarks(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: TopicMarkFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(topicMarkFacade.getAllTopicMarks(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(topicMarkFacade.getTopicMarkByIds(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Get last 5 Topic Mark by Course", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/course/{id}")
    @CrossOrigin
    fun getLast5TopicMarkByCourse(
        @PathVariable id: Long,
    ): ResponseEntity<List<TopicMarkDto>> {
        return ResponseEntity(topicMarkFacade.getLast5MarksByCourse(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Get last 5 Topic Mark by Student", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/student/{id}")
    @CrossOrigin
    fun getLast5TopicMarkByStudent(
        @PathVariable id: String,
    ): ResponseEntity<List<TopicMarkDto>> {
        return ResponseEntity(topicMarkFacade.getLast5MarksByUser(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Get Topic Mark by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getTopicById(
        @PathVariable id: Long,
    ): ResponseEntity<TopicMarkDto> {
        return ResponseEntity(topicMarkFacade.getTopicMarkById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete topicMark by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteTopic(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(topicMarkFacade.deleteTopicMark(id), HttpStatus.OK)
    }
}