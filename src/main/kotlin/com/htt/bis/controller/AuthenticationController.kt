package com.htt.bis.controller

import com.htt.bis.config.JWT_TOKEN_API_KEY_NAME
import com.htt.bis.dto.auth.*
import com.htt.bis.service.AuthenticationService
import io.swagger.annotations.*
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.authorization.client.util.HttpResponseException
import org.keycloak.representations.AccessTokenResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

const val AUTHENTICATION_BASE_URL = "/auth"

@RestController
@RequestMapping(AUTHENTICATION_BASE_URL)
@Api(value = "Authentication", tags = ["Authentication endpoints"])
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @ApiOperation(value = "Sign in user", nickname = "signIn", response = AccessTokenResponse::class, tags = ["Authentication endpoints"])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message =    "User successfully signed in", response = AccessTokenResponse::class),
            ApiResponse(code = 401, message = "Unauthorized. User provided bad credentials")
        ]
    )
    @PostMapping("/sign-in", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun signIn(@RequestBody signInRequest: SignInRequest): ResponseEntity<AuthDto>  {
        return ResponseEntity.ok(authenticationService.signIn(signInRequest))
    }

    @ApiOperation(value = "Sign out user", nickname = "signOut", response = ResponseEntity::class, tags = ["Authentication endpoints"], authorizations = [Authorization(JWT_TOKEN_API_KEY_NAME)])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "User successfully signed out"),
            ApiResponse(code = 400, message = "Invalid token provided")
        ]
    )
    @PostMapping("/sign-out", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun signOut(@RequestBody signOutRequest: SignOutRequest): ResponseEntity<Any> {
        authenticationService.signOut(signOutRequest)
        return ResponseEntity.noContent().build()
    }

    @ApiOperation(value = "Refresh token", nickname = "refreshToken", response = TokenDto::class, tags = ["Authentication endpoints"])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Token refreshed successfully", response = TokenDto::class),
            ApiResponse(code = 400, message = "Invalid token provided")]
    )
    @PostMapping("/refresh")
    @CrossOrigin
    fun refreshToken(@RequestBody refreshToken: RefreshTokenRequest): ResponseEntity<TokenDto>  {
        val accessTokenResponse = authenticationService.refreshToken(refreshToken)
        return ResponseEntity.ok(accessTokenResponse)
    }

}
