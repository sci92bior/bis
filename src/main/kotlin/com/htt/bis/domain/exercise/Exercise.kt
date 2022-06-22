package com.htt.bis.domain.exercise

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Destruction
import com.htt.bis.domain.User
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "exercise")
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    val name : String,

    @Column(name = "start_date")
    val startDate: LocalDateTime,

    @Column(name = "end_date")
    val endDate: LocalDateTime
) : BaseEntity<Long>(){
    @OneToMany(mappedBy = "exercise")
    val databaseItems: MutableSet<DatabaseItemQuantity> = mutableSetOf()


}
