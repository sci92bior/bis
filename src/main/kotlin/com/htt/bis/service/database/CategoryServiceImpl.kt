package com.htt.bis.service.database

import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.BuildMaterialRepository
import com.htt.bis.repository.CategoryRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val sortService: SortService,
) :CategoryService {
    override fun getByName(name: String): Category? {
        return categoryRepository.findByName(name)
    }

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Category> {
        val sort = sortService.buildSortFromSortStrings(sortStrings,Category::class.java)
        return categoryRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Category? {
        return categoryRepository.findByIdOrNull(id)
    }

    override fun add(model:Category):Category {
        if(categoryRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return categoryRepository.save(model)
    }

    override fun update(model:Category):Category {
        return categoryRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            categoryRepository.delete(it)
        }
    }

    override fun count() : Long {
       return categoryRepository.count()
    }

}
