package com.htt.bis.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean


@NoRepositoryBean
interface SoftDeletableJpaRepository<T_ENTITY, ID_TYPE> : JpaRepository<T_ENTITY, ID_TYPE> {
    @Query("update #{#entityName} e set e.isDeleted = true where e.id = ?1")
    @Modifying
    fun softDeleteById(id: ID_TYPE)

    @Query("update #{#entityName} e set e.isDeleted = false where e.id = ?1")
    @Modifying
    fun restoreSoftDeleteById(id: ID_TYPE)

    @Query("select e from #{#entityName} e where e.isDeleted = true")
    fun findSoftDeleted(pageRequest: PageRequest): Page<T_ENTITY>

    @Query("select e from #{#entityName} e where e.isDeleted = false")
    override fun findAll(): List<T_ENTITY>

    @Query("select e from #{#entityName} e where e.isDeleted = false")
    fun findAll(pageRequest: PageRequest): Page<T_ENTITY>
}
