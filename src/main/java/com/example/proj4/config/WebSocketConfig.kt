package com.example.proj4.config

import com.example.proj4.dao.response.GetOnlineUserInfosResponse
import com.example.proj4.dao.response.GetOnlineUsersResponse
import com.example.proj4.entity.User
import com.example.proj4.service.JwtService
import com.example.proj4.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.util.UriComponentsBuilder

@Configuration
@EnableWebSocketMessageBroker
open class WebSocketConfig(
    val jwtService: JwtService,
    val userService: UserService,
    @Lazy val messagingTemplate: SimpMessagingTemplate
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic", "/queue")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/webs")
            .addInterceptors(JwtHandshakeInterceptor(jwtService, userService))

    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(NotifyAboutNewOnlineUserInterceptor(userService, jwtService, messagingTemplate))
    }

    internal class JwtHandshakeInterceptor(val jwtService: JwtService, val userService: UserService) : HandshakeInterceptor {


        override fun beforeHandshake(
            request: ServerHttpRequest,
            response: ServerHttpResponse,
            wsHandler: WebSocketHandler,
            attributes: MutableMap<String, Any>
        ): Boolean {
            val uriComponents = UriComponentsBuilder.fromHttpRequest(request).build()
            val jwtToken = uriComponents.queryParams.getFirst("jwtToken")

            println("Trying to connect to WS with Authorization header: $jwtToken")

            return if (jwtToken?.let { jwtService.isTokenValid(it) } == true) {
                attributes["jwtToken"] = jwtToken
                val username = jwtService.extractUserName(jwtToken)
                val user = userService.getUserByUsername(username).orElseThrow()
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, jwtToken)
                true
            } else {
                response.headers.add("WebSocket-Error", "Invalid JWT token")
                false
            }
        }

        override fun afterHandshake(
            request: ServerHttpRequest,
            response: ServerHttpResponse,
            wsHandler: WebSocketHandler,
            exception: Exception?
        ) {
            // Do nothing after the handshake
        }
    }



    internal class NotifyAboutNewOnlineUserInterceptor(
        val userService: UserService,
        val jwtService: JwtService,
        val messagingTemplate: SimpMessagingTemplate
    ) : ChannelInterceptor {

        override fun postSend(
            message: Message<*>,
            channel: MessageChannel,
            sent: Boolean
        ) {
            val accessor = StompHeaderAccessor.wrap(message)


            when(accessor.command) {
                StompCommand.CONNECT -> {
                    val jwtToken = accessor.getNativeHeader("Authorization")?.firstOrNull()?.substring(7)

                    jwtToken?.let {
                        if(!jwtService.isTokenValid(jwtToken)) throw IllegalArgumentException("Invalid jwt token")
                        val username = jwtService.extractUserName(jwtToken)
                        val user = userService.getUserByUsername(username).orElseThrow()
                        accessor.user = UsernamePasswordAuthenticationToken(user, jwtToken)
                        user.online = true
                        userService.save(user)
                        val response =
                            GetOnlineUserInfosResponse(userService.getOnlineUsersWithLastActivity())
                        messagingTemplate.convertAndSend("/topic/webs-topic", response)

                    } ?: throw IllegalArgumentException("JWT token is missing")
                }
                StompCommand.DISCONNECT -> {
                    val user = (accessor.user as UsernamePasswordAuthenticationToken).principal as User
                    user.online = false
                    userService.save(user)
                    val response =
                        GetOnlineUserInfosResponse(userService.getOnlineUsersWithLastActivity())
                    messagingTemplate.convertAndSend("/topic/webs-topic", response)
                }
                else -> Unit
            }
        }
    }
}
