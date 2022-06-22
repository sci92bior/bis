package com.htt.bis.service.database

import com.htt.bis.domain.Destruction
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.DestructionRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DestructionServiceImpl(
    private val buildMaterialRepository: DestructionRepository,
    private val sortService: SortService,
) : DestructionService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Destruction> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, Destruction::class.java)
        return buildMaterialRepository.findAll(query,PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Destruction? {
        return buildMaterialRepository.findByIdOrNull(id)
    }

    override fun add(model: Destruction): Destruction {
        return buildMaterialRepository.save(model)
    }

    override fun update(model: Destruction): Destruction {
        return buildMaterialRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            buildMaterialRepository.delete(it)
        }
    }

    override fun count() : Long {
       return buildMaterialRepository.count()
    }

}
