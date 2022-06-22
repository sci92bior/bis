package com.htt.bis.repository

import com.htt.bis.domain.core.Tool

interface ToolRepository : JpaRepositoryWithQuerydslPredicate<Tool, Long>
