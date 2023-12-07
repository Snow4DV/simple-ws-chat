package com.example.proj4.service

import com.example.proj4.entity.Message
import java.time.LocalDateTime

interface MessageService {
    fun saveMessage(message: Message)
    fun getMessagesSortedByDate(): List<Message>
    fun getLatestMessageTimeByUserId(userId: Long): LocalDateTime?
}