package com.htt.bis.domain.exercise

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Destruction
import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.User
import com.htt.bis.domain.course.Topic
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
    val endDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_topic_exercise", referencedColumnName = "id")
    val topic: Topic? = null
) : BaseEntity<Long>(){
    @OneToMany(mappedBy = "exercise")
    val databaseItems: MutableSet<DatabaseItemQuantity> = mutableSetOf()


}
