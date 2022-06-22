package com.htt.bis.repository

import com.htt.bis.domain.User

interface UserRepository : JpaRepositoryWithQuerydslPredicate<User, Long>{
    fun findByKeycloakId(keycloakId : String) : User?
    fun deleteUserByKeycloakId(keycloakId: String)
}
