package com.htt.bis.dto.user

import javax.validation.constraints.Email
import javax.validation.constraints.Pattern


class CreateUserRequest(
    @get:Pattern(
        regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]\$",
        message = "Username length must be between 5 and 20 characters. \" +\n" +
            "\"Username can contain following characters: a-z, A-Z, 0-9, dot(.), hyphen(-) and underscore(_)")
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val dutyTitle: String,
    @get:Email(message = "Provided email is wrong")
    val email: String,
    val roles: List<String> = mutableListOf()
)

