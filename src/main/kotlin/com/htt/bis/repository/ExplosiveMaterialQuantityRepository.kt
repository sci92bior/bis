package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ExplosiveMaterialQuantity
import com.htt.bis.domain.ExplosiveUnit

interface ExplosiveMaterialQuantityRepository : JpaRepositoryWithQuerydslPredicate<ExplosiveMaterialQuantity, Long>{
    fun findByExplosiveUnit(explosiveUnit: ExplosiveUnit) : List<ExplosiveMaterialQuantity>
}
