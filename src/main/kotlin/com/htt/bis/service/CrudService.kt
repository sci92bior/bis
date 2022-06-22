package com.htt.bis.service

import com.htt.bis.domain.BaseEntity
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface CrudService<MODEL : BaseEntity<*>> {

    fun getAll(query: BooleanExpression, page : Int, size : Int, sortStrings: List<String>?): Page<MODEL>

    fun getById(id : Long): MODEL?

    fun add(model: MODEL): MODEL

    fun update(model: MODEL): MODEL

    fun delete(id: Long)

    fun count() : Long

}
