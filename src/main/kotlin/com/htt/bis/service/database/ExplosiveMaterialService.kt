package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.service.CrudService

interface ExplosiveMaterialService : CrudService<ExplosiveMaterial>{
    fun getThumbnail(id : String) : String
    fun getFullImage(id : String) : String
}