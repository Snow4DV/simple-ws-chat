package com.example.proj4.service.impl

import com.example.proj4.entity.Message
import com.example.proj4.repository.MessageRepository
import com.example.proj4.service.MessageService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageServiceImpl(
    val messageRepository: MessageRepository
) : MessageService {

    override fun saveMessage(message: Message) {
        messageRepository.save(message)
    }

    override fun getMessagesSortedByDate(): List<Message> {
        return messageRepository.findAllByOrderByTimestamp()
    }

    override fun getLatestMessageTimeByUserId(userId: Long): LocalDateTime? {
        return messageRepository.findTopBySenderIdOrderByTimestampDesc(userId)?.timestamp
    }
}