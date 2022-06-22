package com.htt.bis.service.database

import com.htt.bis.domain.*
import com.htt.bis.service.CrudService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page

interface BuildMaterialQuantityService {
    fun getByObstacle(obstacle: Obstacle) : List<BuildMaterialQuantity>
    fun getAll(page : Int, size : Int, sortStrings: List<String>?): Page<BuildMaterialQuantity>

    fun getById(id : Long): BuildMaterialQuantity?

    fun add(model: BuildMaterialQuantity): BuildMaterialQuantity

    fun update(model: BuildMaterialQuantity): BuildMaterialQuantity

    fun delete(id: Long)

    fun count() : Long
}