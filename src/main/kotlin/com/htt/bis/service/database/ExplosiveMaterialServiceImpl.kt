package com.htt.bis.service.database

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ExplosiveMaterialRepository
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
class ExplosiveMaterialServiceImpl(
    private val explosiveMaterialRepository: ExplosiveMaterialRepository,
    private val sortService: SortService,
    private val storageService : StorageService
) : ExplosiveMaterialService {
    override fun getThumbnail(id: String): String {
        TODO("Not yet implemented")
    }

    override fun getFullImage(id: String): String {
        TODO("Not yet implemented")
    }

    override fun getAll(query: BooleanExpression, page : Int, size: Int, sortStrings: List<String>?): Page<ExplosiveMaterial> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, ExplosiveMaterial::class.java)
        return explosiveMaterialRepository.findAll(query, PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): ExplosiveMaterial? {
        return explosiveMaterialRepository.findByIdOrNull(id)
    }

    override fun add(model: ExplosiveMaterial): ExplosiveMaterial {
        if(explosiveMaterialRepository.findByName(model.name)!=null){
            throw ExplosiveMaterialAlreadyExistException()
        }
        return explosiveMaterialRepository.save(model)
    }

    override fun update(model: ExplosiveMaterial): ExplosiveMaterial {
        return explosiveMaterialRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            explosiveMaterialRepository.delete(it)
        }
    }

    override fun count() : Long {
       return explosiveMaterialRepository.count()
    }

}
