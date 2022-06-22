package com.htt.bis.service.database

import com.htt.bis.domain.ProcessItem
import com.htt.bis.exception.ExplosiveMaterialAlreadyExistException
import com.htt.bis.facade.ifFound
import com.htt.bis.repository.ProcessItemRepository
import com.htt.bis.service.SortService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ProcessItemServiceImpl(
    private val processItemRepository: ProcessItemRepository,
    private val sortService: SortService,
) : ProcessItemService {

    override fun getAll(page : Int, size: Int, sortStrings: List<String>?): Page<ProcessItem> {
        val sort = sortService.buildSortFromSortStrings(sortStrings, ProcessItem::class.java)
        return processItemRepository.findAll(PageRequest.of(page, size, sort))
    }

    override fun getById(id: Long): ProcessItem? {
        return processItemRepository.findByIdOrNull(id)
    }

    override fun add(model: ProcessItem): ProcessItem {
        return processItemRepository.save(model)
    }

    override fun update(model: ProcessItem): ProcessItem {
        return processItemRepository.save(model)
    }

    override fun delete(id: Long) {
        getById(id).ifFound {
            processItemRepository.delete(it)
        }
    }

    override fun count() : Long {
       return processItemRepository.count()
    }

}
