package com.htt.bis.facade.user

import com.htt.bis.domain.Role
import com.htt.bis.domain.User
import com.htt.bis.dto.user.CreateUserRequest
import com.htt.bis.dto.user.UpdateUserRequest
import com.htt.bis.dto.user.UserDTO
import com.htt.bis.dto.user.UserFilter
import com.htt.bis.facade.Facade
import com.htt.bis.facade.ifFound
import com.htt.bis.service.KeycloakRoleService
import com.htt.bis.service.KeycloakUserService
import com.htt.bis.service.UserService
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.util.*


@Facade
@Service
class UserFacade(
    private val keycloakUserService: KeycloakUserService,
    private val keycloakRoleService: KeycloakRoleService,
    private val userService: UserService
) {

    fun getAllUsers(filter : UserFilter?, pageNo: Int?, pageSize: Int?): Any {

        if(filter!!.ids!=null){
            return getUsersById(filter.ids!!)
        }else if(filter.role!=null){
            val userList = keycloakRoleService.findUsersByRole(filter.role).ifFound { list ->
                list.map {
                    mapKeycloakUserToUserDTO(it)
                }
            }
            val pageable: Pageable = PageRequest.of(pageNo!!, pageSize!!)
            return PageImpl(userList, pageable, userList.count().toLong())
        }else {
            val userList = keycloakUserService.findAll().ifFound { list ->
                list.map {
                    mapKeycloakUserToUserDTO(it)
                }
            }
            val pageable: Pageable = PageRequest.of(pageNo!!, pageSize!!)
            return PageImpl(userList, pageable, userList.count().toLong())
        }
    }

    fun getUsersById(ids : Array<String>): List<UserDTO> {
        var users = mutableListOf<UserDTO>()
        ids.forEach { id ->
            val user = getUserById(UUID.fromString(id))
            users.add(user)
        }
        print(users)
        return users
    }

    fun getUserById(userId: UUID): UserDTO {
        return keycloakUserService.findById(userId.toString()).ifFound {
            mapKeycloakUserToUserDTO(it)
        }
    }
    
    fun getUsersByRole(role: Role): List<UserDTO> {
        return keycloakRoleService.findUsersByRole(role.name).ifFound { list ->
            list.map {
                mapKeycloakUserToUserDTO(it)
            }
        }
    }

    fun createUser(createUserRequest: CreateUserRequest): UserDTO {
        if (keycloakUserService.findByUsername(createUserRequest.username).isEmpty()) {
            if (keycloakUserService.findByEmail(createUserRequest.email).isEmpty()) {
                val keycloakRoles = keycloakRoleService.findAll()
                createUserRequest.roles.forEach { role ->
                    if (keycloakRoles.none { it.name == role })
                        throw Error("Provided role [${role}] for user does not exist")
                }
                keycloakUserService.createUser(createUserRequest, false)
                val keycloakUser = keycloakUserService.findByUsername(createUserRequest.username)
                if (keycloakUser.size == 1) {
                    createUserRequest.roles.forEach {
                        keycloakUserService.assignRole(keycloakUser[0].id, keycloakRoleService.findByName(it))
                    }
                    userService.createUser(User(keycloakId = keycloakUser[0].id))

                    return mapKeycloakUserToUserDTO(keycloakUser[0])
                } else
                    throw Error("User duplication with provided username")
            } else
                throw Error("User with provided email address already exists")
        } else
            throw Error("User with provided username already exists")
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): UserDTO {
        keycloakUserService.findById(updateUserRequest.id).ifFound { user ->
            user.email = updateUserRequest.email
            user.firstName = updateUserRequest.firstName
            user.lastName = updateUserRequest.lastName
            user.isEnabled=updateUserRequest.enabled
            user.singleAttribute("dutyTitle", updateUserRequest.dutyTitle)
            keycloakUserService.deleteAllRoles(user.id)
            updateUserRequest.roles.forEach {
                keycloakUserService.assignRole(user.id, keycloakRoleService.findByName(it))
            }
            keycloakUserService.updateUser(user)
            return mapKeycloakUserToUserDTO(keycloakUserService.findById(updateUserRequest.id))
        }
    }

    fun deleteUser(userId: String) {
        keycloakUserService.findById(userId).ifFound {
            keycloakUserService.deleteUser(it)
        }
    }

    fun enableUser(userId: String): UserDTO {
        keycloakUserService.findById(userId).ifFound {
            keycloakUserService.enableUser(it)
            return mapKeycloakUserToUserDTO(keycloakUserService.findById(userId))
        }
    }

    fun disableUser(userId: String): UserDTO {
        keycloakUserService.findById(userId).ifFound {
            keycloakUserService.disableUser(it)
            return mapKeycloakUserToUserDTO(keycloakUserService.findById(userId))
        }
    }

    private fun mapKeycloakUserToUserDTO(keycloakUser: UserRepresentation): UserDTO {
        val roles = keycloakRoleService.findRolesByUserId(keycloakUser.id).map { it.name }
        return UserDTO(
            id = keycloakUser.id,
            username = keycloakUser.username,
            firstName = keycloakUser.firstName,
            lastName = keycloakUser.lastName,
            email = keycloakUser.email,
            enabled = keycloakUser.isEnabled,
            roles = roles,
            dutyTitle = keycloakUser.firstAttribute("dutyTitle"),
            createdDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(keycloakUser.createdTimestamp), TimeZone.getDefault().toZoneId()).toString(),
        )
    }

}
