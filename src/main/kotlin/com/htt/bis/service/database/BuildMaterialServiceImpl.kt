package com.htt.bis.service.database

import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.BuildMaterialRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class BuildMaterialServiceImpl(
    private val buildMaterialRepository: BuildMaterialRepository,
    private val sortService: SortService,
) : BuildMaterialService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<BuildMaterial> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, BuildMaterial::class.java)
        return buildMaterialRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): BuildMaterial? {
        return buildMaterialRepository.findByIdOrNull(id)
    }

    override fun add(model: BuildMaterial): BuildMaterial {
        if(buildMaterialRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return buildMaterialRepository.save(model)
    }

    override fun update(model: BuildMaterial): BuildMaterial {
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
