package com.htt.bis.common

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus

class ErrorResponse {

    lateinit var message: String

    var details: String? = null

    var code: Int? = null

    @field: JsonIgnore
    lateinit var httpStatus: HttpStatus

    /*
    Do not use this constructor. It is only for deserializing from json in tests.
     */
    constructor()

    private constructor(message: String, details: String?, code: Int, httpStatus: HttpStatus) {
        this.message = message
        this.details = details
        this.code = code
        this.httpStatus = httpStatus
    }

    object Generic {

        //MESSAGES
        const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error"
        const val CANNOT_SORT_MESSAGE = "Cannot sort"
        const val ARGUMENT_TYPE_MISMATCH_MESSAGE = "Argument type mismatch"
        const val VALIDATION_FAILED_MESSAGE = "Validation Failed"
        const val ENTITY_AT_REVISION_NOT_FOUND_MESSAGE = "Entity at revision not found"
        const val UNABLE_TO_CONNECT_TO_SERVICE_MESSAGE = "Unable to connect to service"
        const val DOMAIN_OBJECT_NOT_FOUND_MESSAGE = "Domain object not found message"
        const val DOMAIN_OBJECT_ALREADY_APPROVED_MESSAGE = "Domain object already approved"

        //CODES
        const val INTERNAL_SERVER_ERROR_CODE = 1
        const val CANNOT_SORT_CODE = 2
        const val ARGUMENT_TYPE_MISMATCH_CODE = 3
        const val VALIDATION_FAILED_CODE = 4
        const val ENTITY_AT_REVISION_NOT_FOUND_CODE = 5
        const val UNABLE_TO_CONNECT_TO_SERVICE_CODE = 6
        const val DOMAIN_OBJECT_NOT_FOUND_CODE = 7
        const val DOMAIN_OBJECT_APPROVED_CODE = 7

        fun internalServerError(): ErrorResponse {
            return ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE, null, INTERNAL_SERVER_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        fun cannotSort(details: String?): ErrorResponse {
            return ErrorResponse(CANNOT_SORT_MESSAGE, details, CANNOT_SORT_CODE, HttpStatus.BAD_REQUEST)
        }

        fun argumentTypeMismatch(details: String?): ErrorResponse {
            return ErrorResponse(ARGUMENT_TYPE_MISMATCH_MESSAGE, details, ARGUMENT_TYPE_MISMATCH_CODE, HttpStatus.BAD_REQUEST)
        }

        fun validationFailed(details: String?): ErrorResponse {
            return ErrorResponse(VALIDATION_FAILED_MESSAGE, details, VALIDATION_FAILED_CODE, HttpStatus.BAD_REQUEST)
        }

        fun entityAtRevisionNotFound(details: String?): ErrorResponse {
            return ErrorResponse(ENTITY_AT_REVISION_NOT_FOUND_MESSAGE, details, ENTITY_AT_REVISION_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }

        fun unableToConnectToService(details: String?): ErrorResponse {
            return ErrorResponse(UNABLE_TO_CONNECT_TO_SERVICE_MESSAGE, details, UNABLE_TO_CONNECT_TO_SERVICE_CODE, HttpStatus.SERVICE_UNAVAILABLE)
        }

        fun domainObjectNotFound(details: String?): ErrorResponse {
            return ErrorResponse(DOMAIN_OBJECT_NOT_FOUND_MESSAGE, details, DOMAIN_OBJECT_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }

        fun domainObjectApproved(details: String?): ErrorResponse {
            return ErrorResponse(DOMAIN_OBJECT_ALREADY_APPROVED_MESSAGE, details, DOMAIN_OBJECT_APPROVED_CODE, HttpStatus.BAD_REQUEST)
        }

    }

    object User {

        //MESSAGES
        const val USER_NOT_FOUND_MESSAGE = "User not found"

        //CODES
        const val USER_NOT_FOUND_CODE = 100


        fun userNotFound(details: String?): ErrorResponse {
            return ErrorResponse(USER_NOT_FOUND_MESSAGE, details, USER_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }
    }

    object ExplosiveMaterial {

        //MESSAGES
        const val EXPLOSIVE_MATERIAL_NOT_FOUND_MESSAGE = "Explosive material not found"
        const val EXPLOSIVE_MATERIAL_ALREADY_EXIST_MESSAGE = "Explosive material already exist"


        //CODES
        const val EXPLOSIVE_MATERIAL_NOT_FOUND_CODE = 300
        const val EXPLOSIVE_MATERIAL_ALREADY_EXIST_CODE = 301


        fun explosiveMaterialNotFound(details: String?): ErrorResponse {
            return ErrorResponse(EXPLOSIVE_MATERIAL_NOT_FOUND_MESSAGE, details, EXPLOSIVE_MATERIAL_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }

        fun explosiveMaterialAlreadyExists(details: String?): ErrorResponse {
            return ErrorResponse(EXPLOSIVE_MATERIAL_ALREADY_EXIST_MESSAGE, details, EXPLOSIVE_MATERIAL_ALREADY_EXIST_CODE, HttpStatus.BAD_REQUEST)
        }

    }

    object Security {

        //MESSAGES
        const val AUTH_NOT_FOUND_MESSAGE = "Authorization Exception"

        //CODES
        const val AUTH_NOT_FOUND_CODE = 200


        fun unauthorized(details: String?): ErrorResponse {
            return ErrorResponse(AUTH_NOT_FOUND_MESSAGE, details, AUTH_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }
    }

    object Devastation {

        //MESSAGES
        const val DEVASTATION_CREATE_REQUEST_ERROR_MESSAGE = "Bad request during devastation creating"

        //CODES
        const val DEVASTATION_CREATE_REQUEST_ERROR_CODE = 501


        fun createDevastationBadRequest(details: String?): ErrorResponse {
            return ErrorResponse(DEVASTATION_CREATE_REQUEST_ERROR_MESSAGE, details, DEVASTATION_CREATE_REQUEST_ERROR_CODE, HttpStatus.BAD_REQUEST)
        }
    }

    object Images {

        //MESSAGES
        const val UPLOAD_EXCEPTION_MESSAGE = "Image upload exception"
        const val IMAGE_NOT_FOUND_MESSAGE = "Image not found"

        //CODES
        const val UPLOAD_EXCEPTION_CODE = 400
        const val IMAGE_NOT_FOUND_CODE = 401

        fun uploadError(details: String?): ErrorResponse {
            return ErrorResponse(UPLOAD_EXCEPTION_MESSAGE, details, UPLOAD_EXCEPTION_CODE, HttpStatus.BAD_REQUEST)
        }

        fun notFound(details: String?): ErrorResponse {
            return ErrorResponse(IMAGE_NOT_FOUND_MESSAGE, details, IMAGE_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        }
    }


}