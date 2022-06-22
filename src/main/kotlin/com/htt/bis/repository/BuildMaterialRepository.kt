package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import org.springframework.stereotype.Repository

interface BuildMaterialRepository : JpaRepositoryWithQuerydslPredicate<BuildMaterial, Long>{
    fun findByName (name : String) : BuildMaterial?
}
