package com.htt.bis.domain

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class BaseEntity<ID_TYPE> {

    abstract val id: ID_TYPE?

}