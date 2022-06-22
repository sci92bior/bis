package com.htt.bis.repository

import com.htt.bis.domain.exercise.Exercise

interface ExerciseRepository : JpaRepositoryWithQuerydslPredicate<Exercise, Long>
