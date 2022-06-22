package com.htt.bis.domain

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "explosive_unit")
data class ExplosiveUnit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "description")
    @field: NotNull
    var description: String,

    @Column(name = "new_actual")
    @field: NotNull
    var newActual: Double,

    @Column(name = "new_tnt")
    @field: NotNull
    var newTnt: Double,

    @Column(name = "make_time")
    @field: NotNull
    var makeTime: Double,

    @Column(name = "msd")
    @field: NotNull
    var msd: Double,

    @Column(name = "explosive_unit_type")
    @Enumerated(EnumType.STRING)
    var explosiveUnitType: ExplosiveUnitType,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    ) : BaseEntity<Long>(){
    @OneToMany(mappedBy = "explosiveUnit")
    val explosiveMaterials: MutableSet<ExplosiveMaterialQuantity> = mutableSetOf()

    @OneToMany(mappedBy = "explosiveUnit")
    val photos: MutableSet<Photo> = mutableSetOf()

    @OneToMany(mappedBy = "explosiveUnit")
    val destructions: MutableSet<Destruction> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExplosiveUnit

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (newActual != other.newActual) return false
        if (newTnt != other.newTnt) return false
        if (makeTime != other.makeTime) return false
        if (msd != other.msd) return false
        if (explosiveUnitType != other.explosiveUnitType) return false
        if (creationDate != other.creationDate) return false
        if (updateDate != other.updateDate) return false
        if (createdBy != other.createdBy) return false
        if (updatedBy != other.updatedBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + newActual.hashCode()
        result = 31 * result + newTnt.hashCode()
        result = 31 * result + makeTime.hashCode()
        result = 31 * result + msd.hashCode()
        result = 31 * result + explosiveUnitType.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + (updateDate?.hashCode() ?: 0)
        result = 31 * result + createdBy.hashCode()
        result = 31 * result + (updatedBy?.hashCode() ?: 0)
        return result
    }


}
