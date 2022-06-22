package com.htt.bis.repository

import com.htt.bis.domain.Obstacle
import com.htt.bis.domain.Photo

interface PhotoRepository : JpaRepositoryWithQuerydslPredicate<Photo, Long>
