package com.example.proj4.repository

import com.example.proj4.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByOrderByTimestamp(): List<Message>

    fun findTopBySenderIdOrderByTimestampDesc(senderId: Long): Message?
}