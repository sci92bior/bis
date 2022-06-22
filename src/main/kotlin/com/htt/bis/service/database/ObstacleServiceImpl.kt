package com.htt.bis.service.database

import com.htt.bis.domain.Obstacle
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ObstacleRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ObstacleServiceImpl(
    private val obstacleRepository: ObstacleRepository,
    private val sortService: SortService,
) : ObstacleService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Obstacle> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, Obstacle::class.java)
        return obstacleRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Obstacle? {
        return obstacleRepository.findByIdOrNull(id)
    }

    override fun add(model: Obstacle): Obstacle {
        if(obstacleRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return obstacleRepository.save(model)
    }

    override fun update(model: Obstacle): Obstacle {
        return obstacleRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            obstacleRepository.delete(it)
        }
    }

    override fun count() : Long {
       return obstacleRepository.count()
    }

}
