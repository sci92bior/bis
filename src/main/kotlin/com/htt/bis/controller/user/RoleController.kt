package com.htt.bis.controller.user

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.user.RoleDto
import com.htt.bis.facade.user.RoleFacade
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/role")
@Api(tags = ["User endpoint"])
internal class RoleController(
    private val roleFacade: RoleFacade
) {

    @ApiOperation(value = "Get all roles", authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successful operation.")
        ]
    )
    @GetMapping
    fun getAllRoles(): ResponseEntity<List<RoleDto>> {
        return ResponseEntity(roleFacade.getAllRoles(), HttpStatus.OK)
    }

}
