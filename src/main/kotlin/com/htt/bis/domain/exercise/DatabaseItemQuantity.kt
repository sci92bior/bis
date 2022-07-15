package com.htt.bis.domain.exercise

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.ExplosiveMaterial
import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.core.SimpleEntity
import com.htt.bis.domain.course.Topic
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "database_item_quantity")
data class DatabaseItemQuantity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_simple_entity_quantity", referencedColumnName = "id")
    val simpleEntity: SimpleEntity? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_explosive_unit_quantity", referencedColumnName = "id")
    val explosiveUnit: ExplosiveUnit? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_explosive_material_quantity", referencedColumnName = "id")
    val explosiveMaterial: ExplosiveMaterial? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_exercise", referencedColumnName = "id")
    val exercise: Exercise,

    @Column(name = "quantity")
    @field: NotNull
    var quantity: Double,

    ) : BaseEntity<Long>()
