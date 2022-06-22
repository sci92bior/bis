package com.htt.bis.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface JpaRepositoryWithQuerydslPredicate<E, ID> : JpaRepository<E, ID>, QuerydslPredicateExecutor<E>
