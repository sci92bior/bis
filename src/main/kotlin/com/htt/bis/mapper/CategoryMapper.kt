package com.htt.bis.mapper

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.core.Category
import com.htt.bis.dto.database.explosive_unit.ExplosiveUnitDto
import com.htt.bis.dto.database.simple.CategoryDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface CategoryMapper {

    fun categoryToCategoryDto(category: Category): CategoryDto

}

/*
@Mapper(componentModel = "spring")
interface AISearcherObjectMapper {

    @Mappings(value = [
        Mapping(source = "ilko.status", target = "ilkoStatus"),
        Mapping(source = "ilko.vector", target = "ilkoVector")
    ])
    fun aiSearcherObjectToAISearcherObjectDTO(aiSearcherObject: AISearcherObject): AISearcherObjectDTO

    fun aiSearcherObjectToSuperAdminAISearcherObjectDTO(aiSearcherObject: AISearcherObject): SuperAdminAISearcherObjectDTO

    fun aiSearcherObjectToFpgaObjectDTO(aiSearcherObject: AISearcherObject): FpgaObjectDTO

    fun aiSearcherObjectToBoardMemberAISearcherObjectDTO(aiSearcherObject: AISearcherObject): BoardMemberAISearcherObjectDTO

    fun fpgaObjectDTOToAISearcherObject(fpgaObjectDTO: FpgaObjectDTO): AISearcherObject

}

fun fpgaObjectDTOToAISearcherObject(fpgaObjectDTO: FpgaObjectDTO): AISearcherObject {
    //TODO: try to force mapstruct to use constructor with id instead of empty
    val aISearcherObject = AISearcherObject(fpgaObjectDTO.id)

    aISearcherObject.version = fpgaObjectDTO.version
    if (fpgaObjectDTO.deleted != null) {
        aISearcherObject.deleted = fpgaObjectDTO.deleted!!
    }
    if (fpgaObjectDTO.objectType != null) {
        aISearcherObject.objectType = AISearcherObjectType.valueOf(fpgaObjectDTO.objectType!!)
    }
    aISearcherObject.name = fpgaObjectDTO.name
    aISearcherObject.firstName = fpgaObjectDTO.firstName
    aISearcherObject.lastName = fpgaObjectDTO.lastName
    aISearcherObject.birthDate = fpgaObjectDTO.birthDate
    aISearcherObject.pesel = fpgaObjectDTO.pesel
    aISearcherObject.nickname = fpgaObjectDTO.nickname
    if (fpgaObjectDTO.crimeType != null) {
        aISearcherObject.crimeType = CrimeType.valueOf(fpgaObjectDTO.crimeType!!)
    }
    aISearcherObject.companyName = fpgaObjectDTO.companyName
    aISearcherObject.companyOwner = fpgaObjectDTO.companyOwner
    val list = fpgaObjectDTO.boardMembers
    if (list != null) {
        aISearcherObject.boardMembersString = HashSet(list)
    }
    aISearcherObject.companyKrsNumber = fpgaObjectDTO.companyKrsNumber
    aISearcherObject.companyRegonNumber = fpgaObjectDTO.companyRegonNumber
    aISearcherObject.companyNipNumber = fpgaObjectDTO.companyNipNumber
    aISearcherObject.eventDate = fpgaObjectDTO.eventDate
    aISearcherObject.eventType = fpgaObjectDTO.eventType
    aISearcherObject.eventResponsiblePerson = fpgaObjectDTO.eventResponsiblePerson
    aISearcherObject.city = fpgaObjectDTO.city
    aISearcherObject.street = fpgaObjectDTO.street
    aISearcherObject.buildingNumber = fpgaObjectDTO.buildingNumber
    aISearcherObject.localNumber = fpgaObjectDTO.localNumber
    aISearcherObject.vehicleBrand = fpgaObjectDTO.vehicleBrand
    aISearcherObject.vehicleRegistrationNumber = fpgaObjectDTO.vehicleRegistrationNumber
    aISearcherObject.vehicleColor = fpgaObjectDTO.vehicleColor
    if (fpgaObjectDTO.vehicleType != null) {
        aISearcherObject.vehicleType = VehicleType.valueOf(fpgaObjectDTO.vehicleType!!)
    }
    aISearcherObject.phoneNumber = fpgaObjectDTO.phoneNumber
    aISearcherObject.phoneModel = fpgaObjectDTO.phoneModel
    aISearcherObject.phoneImeiNumber = fpgaObjectDTO.phoneImeiNumber
    aISearcherObject.phoneImsiNumber = fpgaObjectDTO.phoneImsiNumber
    aISearcherObject.email = fpgaObjectDTO.email
    aISearcherObject.computerNumber = fpgaObjectDTO.computerNumber
    aISearcherObject.documentType = fpgaObjectDTO.documentType
    aISearcherObject.documentNumber = fpgaObjectDTO.documentNumber
    aISearcherObject.bankAccountNumber = fpgaObjectDTO.bankAccountNumber
    aISearcherObject.bankName = fpgaObjectDTO.bankName
    aISearcherObject.substanceName = fpgaObjectDTO.substanceName
    aISearcherObject.substanceAmount = fpgaObjectDTO.substanceAmount
    aISearcherObject.exciseProductType = fpgaObjectDTO.exciseProductType
    aISearcherObject.exciseProductName = fpgaObjectDTO.exciseProductName
    if (fpgaObjectDTO.exciseProductAmount != null) {
        aISearcherObject.exciseProductAmount = fpgaObjectDTO.exciseProductAmount.toString()
    }

    return aISearcherObject
}*/
