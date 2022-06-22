package com.htt.bis.domain

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "obstacle")
data class Obstacle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "thickness")
    @field: NotNull
    var thickness: Double,

    @Column(name = "description")
    @field: NotNull
    var description: String,

    @Column(name = "obstacle_type")
    @Enumerated(EnumType.STRING)
    var obstacleType : ObstacleType,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    ) : BaseEntity<Long>(){

    @OneToMany(mappedBy = "obstacle")
    val destructions: MutableSet<Destruction> = mutableSetOf()

    @OneToMany(mappedBy = "obstacle")
    val photos: MutableSet<Photo> = mutableSetOf()

    @OneToMany(mappedBy = "obstacle")
    val buildMaterialQuantity: MutableSet<BuildMaterialQuantity> = mutableSetOf()
}
