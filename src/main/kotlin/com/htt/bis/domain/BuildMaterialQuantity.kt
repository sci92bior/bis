package com.htt.bis.domain

import com.htt.bis.domain.core.BuildMaterial
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "build_material_quantity")
data class BuildMaterialQuantity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_obstacle", referencedColumnName = "id")
    val obstacle: Obstacle,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn( referencedColumnName = "id")
    val buildMaterial: BuildMaterial,

    @Column(name = "quantity")
    @field: NotNull
    var quantity: Double,
) : BaseEntity<Long>()
