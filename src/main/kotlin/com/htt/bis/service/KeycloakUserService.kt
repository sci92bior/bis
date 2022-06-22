package com.htt.bis.service

import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.htt.bis.dto.user.CreateUserRequest
import com.htt.bis.dto.user.UserDTO
import javax.ws.rs.core.Response

@Service
class KeycloakUserService(
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}") private val realm: String
) {
    fun findAll(): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .list()

    fun findByUsername(username: String): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .search(username)

    fun findByEmail(email: String): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .search(email, 0, 1)

    fun findById(id: String): UserRepresentation =
        keycloak
            .realm(realm)
            .users()
            .get(id)
            .toRepresentation()


    fun assignRole(userId: String, roleRepresentation: RoleRepresentation) {
        keycloak
            .realm(realm)
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .add(listOf(roleRepresentation))
    }

    fun deleteAllRoles(userId: String) {
        val roles = keycloak
            .realm(realm)
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .listAll()
        keycloak
            .realm(realm)
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .remove(roles)
    }

    fun create(userDTO: UserDTO): Response {
        val user = prepareUserRepresentation(userDTO)

        return keycloak
            .realm(realm)
            .users()
            .create(user)
    }

    fun createUser(createUserRequest: CreateUserRequest, enabled: Boolean): Response {
        val password = preparePasswordRepresentation(createUserRequest.password)
        val user = createUserRepresentation(createUserRequest, password, enabled)

        return keycloak
            .realm(realm)
            .users()
            .create(user)
    }

    private fun prepareUserRepresentation(userDTO: UserDTO): UserRepresentation {
        val newUser = UserRepresentation()
        newUser.id = userDTO.id
        newUser.username = userDTO.username
        newUser.firstName = userDTO.firstName
        newUser.isEnabled = true
        return newUser
    }

    private fun createUserRepresentation(createUserRequest: CreateUserRequest, cR: CredentialRepresentation, enabled: Boolean): UserRepresentation {
        val newUser = UserRepresentation()
        newUser.username = createUserRequest.username
        newUser.firstName = createUserRequest.firstName
        newUser.lastName = createUserRequest.lastName
        newUser.email = createUserRequest.email
        newUser.credentials = listOf(cR)
        newUser.isEnabled = enabled
        newUser.singleAttribute("dutyTitle", createUserRequest.dutyTitle)
        return newUser
    }

    private fun preparePasswordRepresentation(password: String): CredentialRepresentation {
        val credentialRepresentation = CredentialRepresentation()
        credentialRepresentation.isTemporary = false
        credentialRepresentation.type = CredentialRepresentation.PASSWORD
        credentialRepresentation.value = password
        return credentialRepresentation
    }

    fun deleteUser(user: UserRepresentation) {
        keycloak.realm(realm).users().delete(user.id)
    }

    fun enableUser(user: UserRepresentation) {
        user.isEnabled = true
        keycloak.realm(realm).users().get(user.id).update(user)
    }

    fun updateUser(user: UserRepresentation) {
        keycloak.realm(realm).users().get(user.id).update(user)
    }

    fun disableUser(user: UserRepresentation) {
        user.isEnabled = false
        keycloak.realm(realm).users().get(user.id).update(user)
    }

}
