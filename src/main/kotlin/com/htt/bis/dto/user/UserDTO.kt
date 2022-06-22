package com.htt.bis.dto.user

import java.time.LocalDateTime

class UserDTO(
    val id: String,
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val createdDate: String,
    val dutyTitle: String? = null,
    val email: String? = null,
    val enabled: Boolean,
    val roles: List<String> = mutableListOf()
)
