package com.htt.bis.service.database

import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.core.SimpleEntity
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.BuildMaterialRepository
import com.htt.bis.repository.SimpleEntityRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class SimpleEntityServiceImpl(
    private val simpleEntityRepository: SimpleEntityRepository,
    private val sortService: SortService,
) :SimpleEntityService {
    override fun getByCategoryAndName(category: Category, name: String): SimpleEntity? {
        return simpleEntityRepository.findByNameAndCategory(name, category)
    }

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<SimpleEntity> {
        val sort = sortService.buildSortFromSortStrings(sortStrings,SimpleEntity::class.java)
        return simpleEntityRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): SimpleEntity? {
        return simpleEntityRepository.findByIdOrNull(id)
    }

    override fun add(model:SimpleEntity):SimpleEntity {
        return simpleEntityRepository.save(model)
    }

    override fun update(model:SimpleEntity):SimpleEntity {
        return simpleEntityRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            simpleEntityRepository.delete(it)
        }
    }

    override fun count() : Long {
       return simpleEntityRepository.count()
    }

}
