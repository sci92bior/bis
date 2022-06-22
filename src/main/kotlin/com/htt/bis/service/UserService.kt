package com.htt.bis.service

import com.htt.bis.domain.User
import com.htt.bis.repository.UserRepository
import mu.KLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val keycloakUserService: KeycloakUserService
) {

    fun findUserById(id : Long) : User? {
        return userRepository.findByIdOrNull(id)
    }

    fun findAll() : List<User> {
        return userRepository.findAll()
    }

    fun createUser(user : User) : User {
        return userRepository.save(user)
    }

    fun updateUser(user : User) : User {
        return userRepository.save(user)
    }

    fun deleteUser(user : User) {
        return userRepository.delete(user)
    }

    companion object : KLogging()
}