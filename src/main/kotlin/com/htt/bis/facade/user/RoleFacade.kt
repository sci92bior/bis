package com.htt.bis.facade.user

import com.htt.bis.dto.user.RoleDto
import com.htt.bis.facade.ifFound
import com.htt.bis.service.KeycloakRoleService
import org.keycloak.representations.idm.RoleRepresentation
import org.springframework.stereotype.Component

@Component
internal class RoleFacade(
    private val keycloakRoleService: KeycloakRoleService,
) {

    fun getAllRoles(): List<RoleDto> {
        return keycloakRoleService.findAll().ifFound { list ->
            list.map {
                mapKeycloakRoleToRoleDTO(it)
            }
        }
    }

    fun mapKeycloakRoleToRoleDTO(roleRepresentation: RoleRepresentation) = RoleDto(
        id = roleRepresentation.id,
        name = roleRepresentation.name
    )
}
