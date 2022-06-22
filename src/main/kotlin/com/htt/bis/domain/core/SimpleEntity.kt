package com.htt.bis.domain.core

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Destruction
import com.htt.bis.domain.Obstacle
import com.htt.bis.domain.Photo
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

    @OneToOne(mappedBy = "simpleEntity")
    var photo: Photo? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id_category_entity", referencedColumnName = "id")
    val category: Category,
): BaseEntity<Long>()
