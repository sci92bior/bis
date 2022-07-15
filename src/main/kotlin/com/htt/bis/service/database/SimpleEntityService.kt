package com.htt.bis.service.database

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.core.SimpleEntity
import com.htt.bis.service.CrudService

interface SimpleEntityService : CrudService<SimpleEntity>{
    fun getByCategoryAndName(category: Category, name: String) : SimpleEntity?
}