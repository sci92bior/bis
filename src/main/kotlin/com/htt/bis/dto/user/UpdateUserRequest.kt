package com.htt.bis.dto.user

import javax.validation.constraints.Email
import javax.validation.constraints.Pattern


class UpdateUserRequest(
    val id : String,
    val firstName: String,
    val lastName: String,
    val enabled: Boolean,
    val dutyTitle: String,
    @get:Email(message = "Provided email is wrong")
    val email: String,
    val roles: List<String> = mutableListOf()
)

