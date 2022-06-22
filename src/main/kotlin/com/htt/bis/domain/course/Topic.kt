package com.htt.bis.domain.course

import com.htt.bis.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "topic")
data class Topic(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "name")
    val name : String,

    @Column(name = "end_date")
    val endDate : LocalDateTime,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "course_id")
    val course: Course? = null
): BaseEntity<Long>() {
    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL])
    var topicMarks: MutableSet<TopicMark> = mutableSetOf()


}