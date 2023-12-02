package com.example.proj4.controller

import com.example.proj4.dao.request.SendMessageRequest
import com.example.proj4.entity.Message
import com.example.proj4.entity.User
import com.example.proj4.service.MessageService
import com.example.proj4.service.UserService
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal
import java.time.LocalDateTime
import java.util.*


@Controller
@RequestMapping("/api/v1/")
class MessageController(
    val userService: UserService,
    val messageService: MessageService,
    val messagingTemplate: SimpMessagingTemplate
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/webs-topic")
    fun send(messageRequest: SendMessageRequest, principal: Principal): Message {
        val sendTime = LocalDateTime.now()
        val currentUser = (principal as UsernamePasswordAuthenticationToken).principal as User
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
