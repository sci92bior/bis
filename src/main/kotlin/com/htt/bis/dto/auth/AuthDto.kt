package com.htt.bis.dto.auth

import org.keycloak.representations.AccessTokenResponse

data class AuthDto(

    val token: TokenDto,
    val firstName: String? = null,
    val lastName: String? = null,
    val dutyTitle: String? = null,
    val email: String? = null,
    val roles: List<String>? = null
)

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn : Long,
)