package com.htt.bis.service

import mu.KotlinLogging
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.authorization.client.AuthzClient
import org.keycloak.authorization.client.util.Http
import org.keycloak.representations.AccessTokenResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import com.htt.bis.config.KeycloakOperation
import com.htt.bis.config.KeycloakRequestConfig
import com.htt.bis.domain.Role
import com.htt.bis.domain.RoleName
import com.htt.bis.dto.auth.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class AuthenticationService(
    private val keycloakRequestConfig: KeycloakRequestConfig,
    private val keycloakUserService: KeycloakUserService,
    private val keycloakRoleService: KeycloakRoleService
) {

    fun signIn(signInRequest: SignInRequest): AuthDto {
        logger.info { ">>>>>>>>>>> User ${signInRequest.username} logs in" }
        val authClient: AuthzClient = AuthzClient.create(keycloakRequestConfig)
        val accessTokenResponse: AccessTokenResponse = authClient.obtainAccessToken(signInRequest.username, signInRequest.password)
        //logger.info { "<<<<<<<<< User ${getCurrentLoggedUserId()} logged" }
        return prepareResponse(accessTokenResponse, signInRequest.username)
    }

    fun signOut(signOutRequest: SignOutRequest) {
        logger.info { ">>>>>>>>> User ${getCurrentLoggedUserId()} is signing out" }
        val url: String = keycloakRequestConfig.getKeycloakEndpointUrl(KeycloakOperation.LOGOUT)
        val http = Http(keycloakRequestConfig) { _, _ -> }
        http.post<Any>(url)
            .authentication()
            .client()
            .form()
            .param(REFRESH_TOKEN_PARAM_NAME, signOutRequest.token)
            .param(CLIENT_ID_PARAM_NAME, keycloakRequestConfig.clientIdConfig)
            .param(CLIENT_SECRET_PARAM_NAME, keycloakRequestConfig.clientSecretConfig)
            .response()
            .execute()
    }

    fun refreshToken(refreshToken: RefreshTokenRequest): TokenDto {
        logger.info { ">>>>>>>>> User ${getCurrentLoggedUserId()} is refreshing token" }
        val url: String = keycloakRequestConfig.getKeycloakEndpointUrl(KeycloakOperation.TOKEN)
        val http = Http(keycloakRequestConfig) { _, _ -> }
        val accessTokenResponse: AccessTokenResponse = http.post<AccessTokenResponse>(url)
            .authentication()
            .client()
            .form()
            .param(GRANT_TYPE_PARAM_NAME, "refresh_token")
            .param(REFRESH_TOKEN_PARAM_NAME, refreshToken.refreshToken)
            .param(CLIENT_ID_PARAM_NAME, keycloakRequestConfig.clientIdConfig)
            .param(CLIENT_SECRET_PARAM_NAME, keycloakRequestConfig.clientSecretConfig)
            .response()
            .json(AccessTokenResponse::class.java)
            .execute()

        return TokenDto(accessTokenResponse.token, accessTokenResponse.refreshToken, accessTokenResponse.expiresIn)
    }

    fun getCurrentLoggedUserId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val userUuid = UUID.fromString(authentication.name)
        logger.info { ">>>>>>>>> Current logged user UUID: $userUuid" }
        return userUuid
    }

    fun isCurrentUserAdmin(): Boolean{
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val authorities = authentication.authorities as List<SimpleGrantedAuthority>
        return authorities.any{it.authority == Role.ADMIN.toString()}
    }

    fun isCurrentUserInstructor(): Boolean{
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val authorities = authentication.authorities as List<SimpleGrantedAuthority>
        return authorities.any{it.authority == Role.INSTRUCTOR.toString()}
    }


    private fun prepareResponse(accessTokenResponse: AccessTokenResponse, username: String): AuthDto {
        val user = keycloakUserService.findByUsername(username)
        val roles = keycloakRoleService.findRolesByUserId(user[0].id)
        if (user.size != 1) {
            throw SecurityException("User not found or more than one user found!")
        }
        return AuthDto(
            token = TokenDto(accessTokenResponse.token, accessTokenResponse.refreshToken, accessTokenResponse.expiresIn),
            dutyTitle = user[0].firstAttribute("dutyTitle"),
            firstName = user[0].firstName,
            lastName = user[0].lastName,
            email = user[0].email,
            roles =roles.map { it.name }.toList(),
        )
    }

    companion object {
        private const val REFRESH_TOKEN_PARAM_NAME = "refresh_token"
        private const val CLIENT_ID_PARAM_NAME = "client_id"
        private const val CLIENT_SECRET_PARAM_NAME = "client_secret"
        private const val GRANT_TYPE_PARAM_NAME = "grant_type"
    }

}
