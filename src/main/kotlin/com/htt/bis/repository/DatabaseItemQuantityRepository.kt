package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import com.htt.bis.domain.exercise.Exercise
import org.springframework.stereotype.Repository

interface DatabaseItemQuantityRepository : JpaRepositoryWithQuerydslPredicate<DatabaseItemQuantity, Long>{
    fun findAllByExerciseAndExplosiveMaterial(exercise : Exercise, explosiveMaterial: ExplosiveMaterial) : List<DatabaseItemQuantity>
}
