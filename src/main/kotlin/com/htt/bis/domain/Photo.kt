package com.htt.bis.domain

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.Obstacle
import com.htt.bis.domain.core.BuildMaterial
import com.htt.bis.domain.core.Category
import com.htt.bis.domain.core.SimpleEntity
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "photo")
data class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "original_path")
    @field: NotNull
    var originalPath: String,

    @Column(name = "thumbnail_path")
    @field: NotNull
    var thumbnailPath: String,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "type")
    @field: NotNull
    var type: String,

    @Column(name = "description")
    @field: NotNull
    var description: String? = null,

    @Column(name = "uploaded_by")
    var uploadedBy: String,

    @Column(name = "upload_date")
    var uploadTime: LocalDateTime,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_explosive_material_photo", referencedColumnName = "id")
    val explosiveMaterial: ExplosiveMaterial? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_build_material_photo", referencedColumnName = "id")
    val buildMaterial: BuildMaterial? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_obstacle_photo", referencedColumnName = "id")
    val obstacle: Obstacle? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_explosive_unit_photo", referencedColumnName = "id")
    val explosiveUnit: ExplosiveUnit? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_destruction_before_photo", referencedColumnName = "id")
    val destructionBefore: Destruction? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_destruction_after_photo", referencedColumnName = "id")
    val destructionAfter: Destruction? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_simpleEntity_photo", referencedColumnName = "id")
    val simpleEntity: SimpleEntity? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_category_photo", referencedColumnName = "id")
    val category: Category? = null,

    )
