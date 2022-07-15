package com.htt.bis.controller.database

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.course.*
import com.htt.bis.editors.CourseFilterEditor
import com.htt.bis.facade.course.CourseFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/course")
@Api(tags = ["Course endpoint"])
internal class CourseController(
    private val courseFacade: CourseFacade,
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(CourseFilter::class.java, CourseFilterEditor())
    }

    @ApiOperation(value = "Create new Course", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Course created."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.POST], path = ["/create"])
    @CrossOrigin
    fun createNewCourse(@Valid @RequestBody createCourseRequest: CreateCourseRequest): ResponseEntity<CourseDto> {
        return ResponseEntity(courseFacade.createCourse(createCourseRequest), HttpStatus.CREATED)
    }


    @ApiOperation(value = "Get all Course", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Course returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @RequestMapping(method = [RequestMethod.GET])
    @CrossOrigin
    fun getAllCategories(
        @RequestParam pageNo: Int?,
        @RequestParam pageSize: Int?,
        @RequestParam(value = "sort", required = false) sort: List<String>?,
        @RequestParam(required = false) filter: CourseFilter
    ): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null){
            ResponseEntity(courseFacade.getAllCourses(filter, pageNo-1, pageSize,sort), HttpStatus.OK)
        }else{
            ResponseEntity(courseFacade.getCoursesById(filter.ids!!), HttpStatus.OK)
        }

    }

    @ApiOperation(value = "Add  mark to course", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Mark added."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/mark")
    @CrossOrigin
    fun addTopicMark(
        @RequestBody createMarkRequest: CreateMarkRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(courseFacade.addMarkToTopic(createMarkRequest), HttpStatus.OK)
    }

    @ApiOperation(value = "Mark topic as end", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Mark added."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @PutMapping("/topic/{topicId}")
    @CrossOrigin
    fun markTopicAsEnded(
        @PathVariable topicId : Long
    ): ResponseEntity<Any> {
        return ResponseEntity(courseFacade.markTopicAsEnded(topicId), HttpStatus.OK)
    }

    @ApiOperation(value = "Get Course by Id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Course returned."),
            ApiResponse(code = 400, message = "Bad request.")
        ]
    )
    @GetMapping("/{id}")
    @CrossOrigin
    fun getCourseById(
        @PathVariable id: Long,
    ): ResponseEntity<CourseDto> {
        return ResponseEntity(courseFacade.getCourseById(id), HttpStatus.OK)
    }


    @ApiOperation(value = "Delete course by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Course deleted operation.", response = Nothing::class)
        ]
    )
    @DeleteMapping("/{id}")
    @CrossOrigin
    fun deleteCourse(@PathVariable id: Long): ResponseEntity<Any>  {
        return ResponseEntity(courseFacade.deleteCourse(id), HttpStatus.OK)
    }
}