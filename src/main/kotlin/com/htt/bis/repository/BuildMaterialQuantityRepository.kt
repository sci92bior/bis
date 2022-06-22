package com.htt.bis.repository

import com.htt.bis.domain.*

interface BuildMaterialQuantityRepository : JpaRepositoryWithQuerydslPredicate<BuildMaterialQuantity, Long>{
    fun findByObstacle(obstacle: Obstacle) : List<BuildMaterialQuantity>
}
