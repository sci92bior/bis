package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ExplosiveMaterialQuantity
import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.service.CrudService

interface ExplosiveMaterialQuantityService : CrudService<ExplosiveMaterialQuantity>{
    fun getByExplosiveUnit(explosiveUnit: ExplosiveUnit) : List<ExplosiveMaterialQuantity>
}