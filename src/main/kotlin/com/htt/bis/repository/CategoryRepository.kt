package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import org.springframework.stereotype.Repository

interface CategoryRepository : JpaRepositoryWithQuerydslPredicate<Category, Long>{
    fun findByName (name : String) : Category?
}
