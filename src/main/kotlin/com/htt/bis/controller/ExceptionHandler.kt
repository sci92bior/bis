package com.htt.bis.controller

import com.htt.bis.common.ErrorResponse
import com.htt.bis.exception.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    var logger: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("User not found: ${ex.message}")
        val errorResponse = ErrorResponse.User.userNotFound(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<ErrorResponse> {
        logger.error("Authentication error : ${ex.message}")
        val errorResponse = ErrorResponse.Security.unauthorized(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }


    @ExceptionHandler(CannotSortByPropertyException::class)
    fun handleCannotSortByPropertyException(ex: CannotSortByPropertyException): ResponseEntity<ErrorResponse> {
        logger.error("Cannot sort by property: ${ex.message}")
        val errorResponse = ErrorResponse.Generic.cannotSort(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(ExplosiveMaterialAlreadyExistException::class)
    fun handleExplosiveMaterialAlreadyExistException(ex: ExplosiveMaterialAlreadyExistException): ResponseEntity<ErrorResponse> {
        logger.error("Explosive material already exist: ${ex.message}")
        val errorResponse = ErrorResponse.ExplosiveMaterial.explosiveMaterialAlreadyExists(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(DomainObjectFoundException::class)
    fun handleDomainObjectFoundException(ex: DomainObjectFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Domain object not found: ${ex.message}")
        val errorResponse = ErrorResponse.Generic.domainObjectNotFound(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(ImageUploadException::class)
    fun handleImageUploadException(ex: ImageUploadException): ResponseEntity<ErrorResponse> {
        logger.error("Error during image upload: ${ex.message}")
        val errorResponse = ErrorResponse.Images.uploadError(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(ImageNotFoundException::class)
    fun handleImageNotFoundException(ex: ImageNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Image not found: ${ex.message}")
        val errorResponse = ErrorResponse.Images.notFound(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(DomainObjectAlreadyApprovedException::class)
    fun handleImageNotFoundException(ex: DomainObjectAlreadyApprovedException): ResponseEntity<ErrorResponse> {
        logger.error("Image not found: ${ex.message}")
        val errorResponse = ErrorResponse.Generic.domainObjectApproved(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

    @ExceptionHandler(DevastationCreationException::class)
    fun handleDevastationCreationException(ex: DevastationCreationException): ResponseEntity<ErrorResponse> {
        logger.error("Image not found: ${ex.message}")
        val errorResponse = ErrorResponse.Devastation.createDevastationBadRequest(ex.message)
        return ResponseEntity(errorResponse, errorResponse.httpStatus)
    }

}