package com.htt.bis.controller.user

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.domain.Role
import com.htt.bis.dto.database.EntityFilter
import com.htt.bis.dto.user.CreateUserRequest
import com.htt.bis.dto.user.UpdateUserRequest
import com.htt.bis.dto.user.UserDTO
import com.htt.bis.editors.EntityFilterEditor
import com.htt.bis.facade.user.UserFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/user")
@Api(tags = ["User endpoint"])
internal class UserController(
    private val userFacade: UserFacade
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(EntityFilter::class.java, EntityFilterEditor())
    }

    @ApiOperation(value = "Get all users", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.")
        ]
    )
    @GetMapping
    @CrossOrigin
    fun getAllUsers(@RequestParam pageNo: Int?,
                    @RequestParam pageSize: Int?,
                    @RequestParam(value = "filter", required = false) filter: EntityFilter?): ResponseEntity<Any> {
        return if(pageNo!=null && pageSize!=null) {
            ResponseEntity(userFacade.getAllUsers(pageNo, pageSize), HttpStatus.OK)
        }else{
            ResponseEntity(userFacade.getUsersById(filter!!.ids!!), HttpStatus.OK)
        }
    }

    @ApiOperation(value = "Get all users", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.")
        ]
    )
    @GetMapping("/list")
    @CrossOrigin
    fun getUsersById(@RequestBody filter : EntityFilter): ResponseEntity<List<UserDTO>> {
        return ResponseEntity(userFacade.getUsersById(filter.ids!!), HttpStatus.OK)
    }

    @ApiOperation(value = "Get user by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<UserDTO>  {
        return ResponseEntity(userFacade.getUserById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete user by id", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: UUID): ResponseEntity<Any>  {
        userFacade.deleteUser(id.toString())
        return ResponseEntity(HttpStatus.OK)
    }

    @ApiOperation(value = "Update user", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id: UUID, @RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<UserDTO>  {
        return ResponseEntity(userFacade.updateUser(updateUserRequest), HttpStatus.OK)
    }

    @ApiOperation(value = "Get user by role", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @GetMapping("/role")
    fun getUserByRole(@RequestParam role: Role): ResponseEntity<List<UserDTO>>  {

        return ResponseEntity(userFacade.getUsersByRole(role), HttpStatus.OK)

    }

    @ApiOperation(value = "Register new user", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Successful operation. User created.", response = UserDTO::class),
            ApiResponse(code = 409, message = "Unsuccessful operation. Username or email conflict.")
        ]
    )
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody createUserRequest: CreateUserRequest): ResponseEntity<UserDTO> {
        return ResponseEntity(userFacade.createUser(createUserRequest), HttpStatus.CREATED)

    }

    @ApiOperation(value = "Enable user", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @PostMapping("/{userId}/enable")
    fun enableUser(@PathVariable("userId") userId: String): ResponseEntity<UserDTO>  {
        return ResponseEntity(userFacade.enableUser(userId), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Disable user", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.", response = UserDTO::class)
        ]
    )
    @PostMapping("/{userId}/disable")
    fun disableUser(@PathVariable("userId") userId: String): ResponseEntity<UserDTO>  {
        return ResponseEntity(userFacade.disableUser(userId), HttpStatus.CREATED)
    }
}