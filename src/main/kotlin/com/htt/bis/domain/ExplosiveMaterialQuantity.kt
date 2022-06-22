package com.htt.bis.domain

import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ExplosiveUnit
import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "explosive_material_quantity")
data class ExplosiveMaterialQuantity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_exploisve_material_quantity", referencedColumnName = "id")
    val explosiveMaterial: ExplosiveMaterial,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_explosive_unit", referencedColumnName = "id")
    val explosiveUnit: ExplosiveUnit? = null,

    @Column(name = "quantity")
    @field: NotNull
    var quantity: Double,

): BaseEntity<Long>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ExplosiveMaterialQuantity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , explosiveMaterial = $explosiveMaterial , explosiveUnit = $explosiveUnit , quantity = $quantity )"
    }
}
