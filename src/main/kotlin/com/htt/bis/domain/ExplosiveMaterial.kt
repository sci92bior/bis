package com.htt.bis.domain

import com.htt.bis.domain.UnitType
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "explosive_material")
data class ExplosiveMaterial(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name", unique = true)
    var name: String,

    @Column(name = "grain")
    var grain: Double,

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    var unitType: UnitType,

    @Column(name = "re_factor")
    var reFactor: Double,

    @Column(name = "is_approved")
    var isApproved: Boolean = false,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @OneToOne(mappedBy = "explosiveMaterial")
    var photo: Photo? = null

    ) : BaseEntity<Long>(){
    @OneToMany(mappedBy = "explosiveMaterial")
    val usedIn: MutableSet<ExplosiveMaterialQuantity> = mutableSetOf()
}
