package com.example.proj4.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
open class Message() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    lateinit var sender: User
    lateinit var timestamp: LocalDateTime
    lateinit var text: String

    constructor(sender: User, timestamp: LocalDateTime, text: String) : this() {
        this.sender = sender
        this.timestamp = timestamp
        this.text = text
    }
}
