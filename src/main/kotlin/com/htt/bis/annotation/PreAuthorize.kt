package com.htt.bis.annotation

import com.htt.bis.domain.RoleName
import org.springframework.security.access.prepost.PreAuthorize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyRole('${RoleName.ADMIN}')")
annotation class AdminAuthorization

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyRole('${RoleName.INSTRUCTOR}')")
annotation class InstructorAuthorization

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyRole('${RoleName.USER}')")
annotation class UserAuthorization


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyRole('${RoleName.ADMIN},${RoleName.USER},${RoleName.INSTRUCTOR}')")
annotation class AllAuthorization