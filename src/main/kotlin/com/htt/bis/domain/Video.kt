package com.htt.bis.domain

import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.Obstacle
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "video")
data class Video(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "path")
    @field: NotNull
    var path: String,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "type")
    @field: NotNull
    var type: String,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_process_item_video", referencedColumnName = "id")
    val processItem: ProcessItem,

)
