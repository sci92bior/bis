package com.htt.bis.repository

import com.htt.bis.domain.ProcessItem

interface ProcessItemRepository : JpaRepositoryWithQuerydslPredicate<ProcessItem, Long>
