package com.htt.bis.repository

import com.htt.bis.domain.core.ExpectedBehaviour

interface ExpectedBehaviourRepository : JpaRepositoryWithQuerydslPredicate<ExpectedBehaviour, Long>
