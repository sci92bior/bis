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

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "topic_id")
    var topic: Topic,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User
): BaseEntity<Long>()