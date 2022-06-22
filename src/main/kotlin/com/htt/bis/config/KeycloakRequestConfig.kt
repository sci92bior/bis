package com.htt.bis.config

import org.keycloak.authorization.client.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import com.htt.bis.config.KeycloakOperation

@Primary
@Component
class KeycloakRequestConfig(
        @Value(value = "\${keycloak.realm}") val realmConfig: String,
        @Value(value = "\${keycloak.resource}") val clientIdConfig: String,
        @Value(value = "\${keycloak.auth-server-url}") val authServerUrlConfig: String,
        @Value(value = "\${keycloak.credentials.secret}") val clientSecretConfig: String
) : Configuration(
        authServerUrlConfig,
        realmConfig,
        clientIdConfig,
        mapOf(
                Pair("secret", clientSecretConfig),
                Pair("grant_type", "password")
        ),
        null
) {

    fun getKeycloakEndpointUrl(operation: KeycloakOperation): String {
        return "$authServerUrl/realms/$realm/protocol/openid-connect/${operation.operationName}"
    }

}
