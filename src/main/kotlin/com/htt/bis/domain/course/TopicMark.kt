package com.htt.bis.domain.course

import com.htt.bis.domain.BaseEntity
import com.htt.bis.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "topic_mark")
data class TopicMark(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "create_date")
    val createDate : LocalDateTime,

    @Column(name = "description")
    val description : String,

    @Column(name = "is_plus")
    val isPlus : Boolean,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "topic_id")
    var topic: Topic,

    @Column(name = "student_id")
    var studentId: String,

    @Column(name = "instructor_id")
    var instructor: String
): BaseEntity<Long>()