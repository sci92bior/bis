package com.htt.bis.service.database

import com.htt.bis.domain.*
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import com.htt.bis.domain.exercise.Exercise
import com.htt.bis.service.CrudService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page

interface DatabaseItemQuantityService {
    fun getAll(page : Int, size : Int, sortStrings: List<String>?): Page<DatabaseItemQuantity>

    fun getById(id : Long): DatabaseItemQuantity?

    fun getByExerciseAndExplosiveMaterial(exercise : Exercise, explosiveMaterial: ExplosiveMaterial): List<DatabaseItemQuantity>?

    fun add(model: DatabaseItemQuantity): DatabaseItemQuantity

    fun update(model: DatabaseItemQuantity): DatabaseItemQuantity

    fun delete(id: Long)

    fun count() : Long
}