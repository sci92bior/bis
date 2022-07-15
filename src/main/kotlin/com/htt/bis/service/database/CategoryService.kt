package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.service.CrudService

interface CategoryService : CrudService<Category>{
    fun getByName(name:String) : Category?
}