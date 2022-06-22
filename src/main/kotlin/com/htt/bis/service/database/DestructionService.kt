package com.htt.bis.service.database

import com.htt.bis.domain.Destruction
import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.service.CrudService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page

interface DestructionService {
    fun getAll(query: BooleanExpression, page : Int, size : Int, sortStrings: List<String>?): Page<Destruction>

    fun getById(id : Long): Destruction?

    fun add(model: Destruction): Destruction

    fun update(model: Destruction): Destruction

    fun delete(id: Long)

    fun count() : Long
}