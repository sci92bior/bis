package com.htt.bis.service.database

import com.htt.bis.domain.exercise.Exercise
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ExerciseRepository
import com.htt.bis.service.SortService
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ExerciseServiceImpl(
    private val exerciseRepository: ExerciseRepository,
    private val sortService: SortService,
) : ExerciseService {

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<Exercise> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, Exercise::class.java)
        return exerciseRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): Exercise? {
        return exerciseRepository.findByIdOrNull(id)
    }

    override fun add(model: Exercise): Exercise {
        /*if(exerciseRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }*/
        return exerciseRepository.save(model)
    }

    override fun update(model: Exercise): Exercise {
        return exerciseRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            exerciseRepository.delete(it)
        }
    }

    override fun count() : Long {
       return exerciseRepository.count()
    }

}
