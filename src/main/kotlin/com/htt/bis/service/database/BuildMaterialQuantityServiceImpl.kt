package com.htt.bis.service.database

import com.htt.bis.domain.BuildMaterialQuantity
import com.htt.bis.domain.Obstacle
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.BuildMaterialQuantityRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class BuildMaterialQuantityServiceImpl(
    private val buildMaterialQuantityRepository: BuildMaterialQuantityRepository,
    private val sortService: SortService,
) : BuildMaterialQuantityService {
    override fun getByObstacle(obstacle: Obstacle): List<BuildMaterialQuantity> {
        return buildMaterialQuantityRepository.findByObstacle(obstacle)
    }

    override fun getAll( page: Int, size: Int, sortStrings: List<String>?): Page<BuildMaterialQuantity> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, BuildMaterialQuantity::class.java)
        return buildMaterialQuantityRepository.findAll(PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): BuildMaterialQuantity? {
        return buildMaterialQuantityRepository.findByIdOrNull(id)
    }

    override fun add(model: BuildMaterialQuantity): BuildMaterialQuantity {
        return buildMaterialQuantityRepository.save(model)
    }

    override fun update(model: BuildMaterialQuantity): BuildMaterialQuantity {
        return buildMaterialQuantityRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            buildMaterialQuantityRepository.delete(it)
        }
    }

    override fun count() : Long {
       return buildMaterialQuantityRepository.count()
    }

}
