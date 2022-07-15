package com.htt.bis.domain.core

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Destruction
import com.htt.bis.domain.Obstacle
import com.htt.bis.domain.Photo
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "simple_entity")
data class SimpleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    var unitType: EntityUnitType,

    @OneToOne(mappedBy = "simpleEntity")
    var photo: Photo? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_category_entity", referencedColumnName = "id")
    val category: Category,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "created_by")
    var createdBy: String,

    ): BaseEntity<Long>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (unitType != other.unitType) return false
        if (category != other.category) return false
        if (creationDate != other.creationDate) return false
        if (createdBy != other.createdBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + unitType.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + createdBy.hashCode()
        return result
    }
}
