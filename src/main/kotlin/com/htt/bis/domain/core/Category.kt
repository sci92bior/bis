package com.htt.bis.domain.core

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Photo
import com.htt.bis.domain.course.Course
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "created_by")
    var createdBy: String,

    @OneToOne(mappedBy = "category")
    var photo: Photo? = null,

    ) : BaseEntity<Long>(){

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    val entities: MutableSet<SimpleEntity> = mutableSetOf()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Category

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
