package com.htt.bis.dto.auth

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccessTokenDto(
        @JsonProperty("access_token")
        val token: String
)