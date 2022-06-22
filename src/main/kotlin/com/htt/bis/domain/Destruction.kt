package com.htt.bis.domain

import com.htt.bis.domain.core.*
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "destruction")
data class Destruction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "destruction_type")
    @Enumerated(EnumType.STRING)
    var destructionType: DestructionType = DestructionType.EXERCISE,

    @Column(name = "performer")
    var performer: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "place")
    var place: String? = null,

    @Column(name = "recommendations")
    var recommendations: String? = null,

    @Column(name = "date")
    var date: LocalDateTime,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_mount_type", referencedColumnName = "id")
    var mountType: MountType? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_expected_effect", referencedColumnName = "id")
    var expectedEffect: ExpectedEffect? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_expected_behaviour", referencedColumnName = "id")
    var expectedBehaviour: ExpectedBehaviour? = null,

    @Column(name = "seal")
    @Min(0)
    @Max(100)
    var seal: Int = 0,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "obstacle_id")
    val obstacle: Obstacle? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "explosive_unit_id")
    val explosiveUnit: ExplosiveUnit? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "initial_system_id")
    val initialSystem: InitiationSystem? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "tool_id")
    val tool: Tool? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "gun_id")
    val gun: Gun? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ammo_id")
    val ammo: Ammunition? = null,

    @Column(name = "go")
    val go: Boolean = false,

    @Column(name = "two_stage")
    val twoStage: Boolean = false,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "second_explosive_unit_id")
    val secondExplosiveUnit: ExplosiveUnit? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "second_tool_id")
    val secondTool: Tool? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "second_gun_id")
    val secondGun: Gun? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime? = null,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    ) : BaseEntity<Long>() {
    @OneToMany(mappedBy = "destructionBefore")
    val photosBefore: MutableSet<Photo> = mutableSetOf()

    @OneToMany(mappedBy = "destructionAfter")
    val photosAfter: MutableSet<Photo> = mutableSetOf()

    @OneToMany(mappedBy = "destruction")
    val processItems: MutableSet<ProcessItem> = mutableSetOf()

}
