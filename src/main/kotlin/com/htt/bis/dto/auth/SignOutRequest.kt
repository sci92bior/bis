package com.htt.bis.dto.auth

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class SignOutRequest(
    @field:NotNull
    @field:NotEmpty
    val token: String,
)
