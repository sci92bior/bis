package com.htt.bis.domain

import com.htt.bis.domain.course.Course
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

@Entity
@Table(name = "bis_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "keycloak_id")
    @field: NotNull
    var keycloakId: String,

    ) : BaseEntity<Long>(){

    @OneToMany(mappedBy = "instructor", cascade = [CascadeType.ALL])
    val coursesAsInstructor: MutableSet<Course> = mutableSetOf()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
