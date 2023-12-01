package com.example.proj4.controller

import com.example.proj4.dao.request.SendMessageRequest
import com.example.proj4.entity.Message
import com.example.proj4.service.MessageService
import com.example.proj4.service.UserService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal
import java.time.LocalDateTime
import java.util.*


@Controller
@RequestMapping("/")
class MessageController(
    val userService: UserService,
    val messageService: MessageService,
    val messagingTemplate: SimpMessagingTemplate
) {
    @GetMapping("/webs")
    fun webs(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "webs"
        return modelAndView
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/webs-topic")
    fun send(messageRequest: SendMessageRequest): Message {
        val sendTime = LocalDateTime.now()
        val currentUser = userService.currentUser.orElseThrow { UsernameNotFoundException("Пользователь не авторизован, или токен неверный") }
        val message = Message(currentUser, sendTime, messageRequest.text)
        messageService.saveMessage(message)
        return message
    }

    @MessageMapping("/chat.subscribe")
    fun subscribe(principal: Principal) {
        val chatHistory = messageService.getMessagesSortedByDate()
        messagingTemplate.convertAndSendToUser(principal.name, "/queue/history", chatHistory)
    }
}
