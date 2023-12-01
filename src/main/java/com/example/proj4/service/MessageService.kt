package com.example.proj4.service

import com.example.proj4.entity.Message
import java.time.LocalDateTime

interface MessageService {
    fun getMessagesBeforeInclusive(timestamp: LocalDateTime): List<Message>
    fun getMessagesInRange(
        startNotInclusive: LocalDateTime,
        endInclusive: LocalDateTime
    ): List<Message>

    fun saveMessage(message: Message)
    fun getMessagesSortedByDate(): List<Message>
}