package com.example.proj4.repository

import com.example.proj4.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface MessageRepository : JpaRepository<Message, Long> {
    fun getMessagesByTimestampLessThanEqualOrderByTimestamp(timestamp: LocalDateTime): List<Message>
    fun getMessagesByTimestampGreaterThanAndTimestampLessThanEqualOrderByTimestamp(
        startNotInclusive: LocalDateTime,
        endInclusive: LocalDateTime
    ): List<Message>

    fun findAllByOrderByTimestamp(): List<Message>
}