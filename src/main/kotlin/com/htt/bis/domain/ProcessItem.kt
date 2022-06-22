package com.htt.bis.domain

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.Obstacle
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "process_item")
data class ProcessItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "title")
    @field: NotNull
    var title: String,

    @Column(name = "description")
    @field: NotNull
    var description: String,

    @Column(name = "time")
    @field: NotNull
    var time: Double,

    @OneToOne(mappedBy = "processItem")
    val video: Video? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id_destruction_process_item", referencedColumnName = "id")
    val destruction: Destruction,
): BaseEntity<Long>()
