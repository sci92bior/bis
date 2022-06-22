package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial

interface ExplosiveMaterialRepository : JpaRepositoryWithQuerydslPredicate<ExplosiveMaterial, Long>{
    fun findByName (name : String) : ExplosiveMaterial?
}
