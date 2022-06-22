package com.htt.bis.dto.auth

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RefreshTokenRequest(
    @field: NotNull
    @field: NotEmpty
    val refreshToken: String,
)
