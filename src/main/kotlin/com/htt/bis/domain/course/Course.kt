package com.htt.bis.domain.course

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.User
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "course")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    val name : String,

    @Column(name = "instructor")
    var instructor: String,

    @Column(name = "start_date")
    val startDate: LocalDateTime,

    @Column(name = "end_date")
    val endDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @ElementCollection
    @CollectionTable(name = "bis_course_participats")
    var participants: Set<String>
) : BaseEntity<Long>() {

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL])
    val topics: MutableSet<Topic> = mutableSetOf()
}
