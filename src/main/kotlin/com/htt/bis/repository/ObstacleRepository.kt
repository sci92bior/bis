package com.htt.bis.repository

import com.htt.bis.domain.Obstacle

interface ObstacleRepository : JpaRepositoryWithQuerydslPredicate<Obstacle, Long>{
    fun findByName(name: String): Obstacle?
}
