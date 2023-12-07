package com.example.proj4.controller

import com.example.proj4.dao.request.GetUserInfoRequest
import com.example.proj4.dao.response.GetUserInfoResponse
import com.example.proj4.service.MessageService
import com.example.proj4.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/user")
class UserController(
    val userService: UserService,
    val messageService: MessageService
) {
    @PostMapping
    fun getUserInfo(@RequestBody request: GetUserInfoRequest): ResponseEntity<GetUserInfoResponse> {
        return ResponseEntity.ok(userService.getUserById(request.userId)?.let {
            GetUserInfoResponse(
                it,
                messageService.getLatestMessageTimeByUserId(request.userId)
            )
        }
        )
    }
    @GetMapping("/actives")
    fun getActiveUsers(): ResponseEntity<List<GetUserInfoResponse>> {
        return ResponseEntity.ok(
            userService.getOnlineUsersWithLastActivity()
        )
    }
}