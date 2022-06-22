package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ExplosiveUnitRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ExplosiveUnitServiceImpl(
    private val explosiveUnitRepository: ExplosiveUnitRepository,
    private val sortService: SortService,
) : ExplosiveUnitService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<ExplosiveUnit> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, ExplosiveUnit::class.java)
        return explosiveUnitRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): ExplosiveUnit? {
        return explosiveUnitRepository.findByIdOrNull(id)
    }

    override fun add(model: ExplosiveUnit): ExplosiveUnit {
        if(explosiveUnitRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return explosiveUnitRepository.save(model)
    }

    override fun update(model: ExplosiveUnit): ExplosiveUnit {
        return explosiveUnitRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            explosiveUnitRepository.delete(it)
        }
    }

    override fun count() : Long {
       return explosiveUnitRepository.count()
    }

}
