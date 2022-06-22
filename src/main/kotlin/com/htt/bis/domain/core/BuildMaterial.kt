package com.htt.bis.domain.core

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.Photo
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "build_material")
data class BuildMaterial(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    @field: NotNull
    var name: String,

    @Column(name = "a_factor")
    @field: NotNull
    var aFactor: Double,

    @Column(name = "is_approved")
    var isApproved: Boolean = false,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime,

    @Column(name = "update_date")
    var updateDate: LocalDateTime,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @OneToOne(mappedBy = "buildMaterial")
    var photo: Photo? = null
): BaseEntity<Long>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BuildMaterial

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , aFactor = $aFactor , isApproved = $isApproved , creationDate = $creationDate , updateDate = $updateDate , createdBy = $createdBy , updatedBy = $updatedBy )"
    }
}
