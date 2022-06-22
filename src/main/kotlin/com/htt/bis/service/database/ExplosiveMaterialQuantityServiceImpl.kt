package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ExplosiveMaterialQuantity
import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ExplosiveMaterialRepository
import com.htt.bis.repository.ExplosiveMaterialQuantityRepository
import com.htt.bis.service.SortService
import com.htt.bis.service.storage.StorageService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ExplosiveMaterialQuantityServiceImpl(
    private val explosiveMaterialQuantityRepository: ExplosiveMaterialQuantityRepository,
    private val sortService: SortService,
) : ExplosiveMaterialQuantityService {
    override fun getByExplosiveUnit(explosiveUnit: ExplosiveUnit): List<ExplosiveMaterialQuantity> {
        return explosiveMaterialQuantityRepository.findByExplosiveUnit(explosiveUnit)
    }

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<ExplosiveMaterialQuantity> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, ExplosiveMaterialQuantity::class.java)
        return explosiveMaterialQuantityRepository.findAll(PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): ExplosiveMaterialQuantity? {
        return explosiveMaterialQuantityRepository.findByIdOrNull(id)
    }

    override fun add(model: ExplosiveMaterialQuantity): ExplosiveMaterialQuantity {
        return explosiveMaterialQuantityRepository.save(model)
    }

    override fun update(model: ExplosiveMaterialQuantity): ExplosiveMaterialQuantity {
        return explosiveMaterialQuantityRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            explosiveMaterialQuantityRepository.delete(it)
        }
    }

    override fun count() : Long {
       return explosiveMaterialQuantityRepository.count()
    }

}
