package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.course.TopicDto
import com.htt.bis.dto.course.TopicFilter
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.editors.TopicFilterEditor
import com.htt.bis.facade.course.TopicFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/topic")
@Api(tags = ["Topic endpoint"])
internal class TopicController(
    private val topicFacade: TopicFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(TopicFilter::class.java, TopicFilterEditor())
    }

    @ApiOperation(value = "Get all Topic", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllTopics(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: TopicFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(topicFacade.getAllTopics(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(topicFacade.getTopicByIds(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Mark topic as end", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Mark added."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/end/{topicId}")
    @CrossOrigin
    fun markTopicAsEnded(
        @PathVariable topicId : Long
    ): ResponseEntity<Any> {
        return ResponseEntity(topicFacade.markTopicAsEnded(topicId), HttpStatus.OK)
    }

    @ApiOperation(value = "Get Topic by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
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
    ): ResponseEntity<TopicDto> {
        return ResponseEntity(topicFacade.getTopicById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete topic by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Topic deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteTopic(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(topicFacade.deleteTopic(id), HttpStatus.OK)
    }
}