package com.htt.bis.service

import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class KeycloakRoleService(
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}") private val realm: String
) {
    fun create(name: String) {
        val role = RoleRepresentation()
        role.name = name

        keycloak
            .realm(realm)
            .roles()
            .create(role)
    }

    fun findAll(): List<RoleRepresentation> =
        keycloak
            .realm(realm)
            .roles()
            .list()

    fun findByName(roleName: String): RoleRepresentation =
        keycloak
            .realm(realm)
            .roles()
            .get(roleName)
            .toRepresentation()

    fun findUsersByRole(roleName: String): Set<UserRepresentation> =
        keycloak
            .realm(realm)
            .roles()
            .get(roleName)
            .roleUserMembers


    fun findRolesByUserId(userId: String): List<RoleRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .listAll()
}
