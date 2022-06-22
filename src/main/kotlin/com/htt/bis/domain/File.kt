package com.htt.bis.domain

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PastOrPresent

@Entity
@Table(name = "idv_file")
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String? = null,

    @field:NotEmpty(message = "messages.validation.constraint.not.empty")
    @Column(name = "path")
    val path : String,

    @Column(name = "upload_date")
    val uploadDate: LocalDateTime,

    @field:NotEmpty(message = "messages.validation.constraint.not.empty")
    @Column(name = "file_type")
    val fileType: String,

    @field:NotEmpty(message = "messages.validation.constraint.not.empty")
    @Column(name = "file_name")
    val fileName : String,

    @Column(name = "file_description")
    val fileDescription : String? = null,


)
