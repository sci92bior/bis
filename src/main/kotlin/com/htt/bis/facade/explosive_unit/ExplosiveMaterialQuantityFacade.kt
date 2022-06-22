package com.htt.bis.facade.explosive_unit

import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.dto.database.explosive_material.ExplosiveMaterialQuantityDto
import com.htt.bis.facade.ifFound
import com.htt.bis.mapper.ExplosiveMaterialQuantityMapper
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.ExplosiveMaterialQuantityService
import com.htt.bis.service.database.ExplosiveUnitService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class ExplosiveMaterialQuantityFacade(
    private val explosiveMaterialQuantityService: ExplosiveMaterialQuantityService,
    private val explosiveUnitService: ExplosiveUnitService,
    private val explosiveMaterialQuantityMapper: ExplosiveMaterialQuantityMapper,
    private val authenticationService: AuthenticationService
) {
    var logger: Logger = LoggerFactory.getLogger(ExplosiveMaterialQuantityFacade::class.java)


    @UserAuthorization
    fun getExplosiveMaterialQuantityByExplosiveUnitId(id: Long): List<ExplosiveMaterialQuantityDto> {
        logger.info("Getting ExplosiveUnits with id $id")
        explosiveUnitService.getById(id).ifFound { explosiveUnit ->
            return explosiveMaterialQuantityService.getByExplosiveUnit(explosiveUnit).map { explosiveMaterialQuantityMapper.explosiveMaterialQuantityObjectToExplosiveMaterialQuantityDTO(it) }
        }
    }

//    @UserAuthorization
//    fun updateExplosiveUnit(id : Long, updateExplosiveUnitRequest: UpdateExplosiveUnitRequest) : ExplosiveUnitDto {
//        logger.info("Updating ExplosiveUnit with name ${updateExplosiveUnitRequest.name}")
//        explosiveUnitService.getById(id).ifFound {
//            var userId = authenticationService.getCurrentLoggedUserId()
//            val explosiveUnitToUpdate = it.copy(id = it.id, name = updateExplosiveUnitRequest.name!!, grain = updateExplosiveUnitRequest.grain!!,
//            reFactor = updateExplosiveUnitRequest.reFactor!!, unitType = updateExplosiveUnitRequest.unitType!!, isApproved = updateExplosiveUnitRequest.isApproved!! ,updateDate = LocalDateTime.now(), updatedBy = userId.toString())
//
//            if(updateExplosiveUnitRequest.photoBase64!=null){
//                storageService.uploadDatabaseImage(ObjectType.EXPLOSIVE_MATERIAL.bucket,updateExplosiveUnitRequest.photoBase64, id.toString())
//            }
//            return explosiveUnitMapper.explosiveUnitObjectToExplosiveUnitDTO(explosiveUnitService.update(explosiveUnitToUpdate))
//        }
//    }

    fun getExplosivesMaterialsQuantityByExplosiveUnitIds(ids: Array<String>): List<ExplosiveMaterialQuantityDto> {
        val explosives = mutableListOf<ExplosiveMaterialQuantityDto>()
        ids.forEach { id ->
            val explosive = getExplosiveMaterialQuantityByExplosiveUnitId(id.toLong())
            explosives.addAll(explosive)
        }
        return explosives
    }

}