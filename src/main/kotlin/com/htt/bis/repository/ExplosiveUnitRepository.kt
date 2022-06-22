package com.htt.bis.repository

import com.htt.bis.domain.ExplosiveUnit

interface ExplosiveUnitRepository : JpaRepositoryWithQuerydslPredicate<ExplosiveUnit, Long>{
    fun findByName(name : String) : ExplosiveUnit?
}
