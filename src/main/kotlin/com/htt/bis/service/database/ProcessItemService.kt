package com.htt.bis.service.database

import com.htt.bis.domain.*
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.service.CrudService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page

interface ProcessItemService {

    fun getAll(page : Int, size : Int, sortStrings: List<String>?): Page<ProcessItem>

    fun getById(id : Long): ProcessItem?

    fun add(model: ProcessItem): ProcessItem

    fun update(model: ProcessItem): ProcessItem

    fun delete(id: Long)

    fun count() : Long

}