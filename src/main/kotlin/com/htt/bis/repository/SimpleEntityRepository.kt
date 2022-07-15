package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.core.SimpleEntity
import org.springframework.stereotype.Repository

interface SimpleEntityRepository : JpaRepositoryWithQuerydslPredicate<SimpleEntity, Long>{
    fun findByNameAndCategory (name : String, category: Category) : SimpleEntity?
}
