package com.htt.bis.repository

import com.htt.bis.domain.Destruction

interface DestructionRepository : JpaRepositoryWithQuerydslPredicate<Destruction, Long>
