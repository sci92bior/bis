package com.htt.bis.domain.course

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.User
import java.time.LocalDateTime
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

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructor_id")
    var instructor: User,

    @Column(name = "start_date")
    val startDate: LocalDateTime,

    @Column(name = "end_date")
    val endDate: LocalDateTime
) : BaseEntity<Long>() {

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "students_id")
    val students: MutableSet<User> = mutableSetOf()

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL])
    val topics: MutableSet<Topic> = mutableSetOf()
}
