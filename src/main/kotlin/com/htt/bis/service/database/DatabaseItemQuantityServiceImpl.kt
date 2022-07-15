package com.htt.bis.service.database

import com.htt.bis.common.ErrorResponse
import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import com.htt.bis.domain.exercise.Exercise
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.DatabaseItemQuantityRepository
import com.htt.bis.service.SortService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DatabaseItemQuantityServiceImpl(
    private val databaseItemQuantityRepository: DatabaseItemQuantityRepository,
    private val sortService: SortService,
) : DatabaseItemQuantityService {


    override fun getAll( page: Int, size: Int, sortStrings: List<String>?): Page<DatabaseItemQuantity> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, DatabaseItemQuantity::class.java)
        return databaseItemQuantityRepository.findAll(PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): DatabaseItemQuantity? {
        return databaseItemQuantityRepository.findByIdOrNull(id)
    }

    override fun getByExerciseAndExplosiveMaterial(exercise: Exercise, explosiveMaterial: ExplosiveMaterial): List<DatabaseItemQuantity>? {
        return databaseItemQuantityRepository.findAllByExerciseAndExplosiveMaterial(exercise, explosiveMaterial)
    }

    override fun add(model: DatabaseItemQuantity): DatabaseItemQuantity {
        return databaseItemQuantityRepository.save(model)
    }

    override fun update(model: DatabaseItemQuantity): DatabaseItemQuantity {
        return databaseItemQuantityRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            databaseItemQuantityRepository.delete(it)
        }
    }

    override fun count() : Long {
       return databaseItemQuantityRepository.count()
    }

}
