package com.example.proj4.controller

import com.example.proj4.dao.request.SignInRequest
import com.example.proj4.dao.request.SignUpRequest
import com.example.proj4.dao.response.JwtAuthenticationResponse
import com.example.proj4.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    val authenticationService: AuthenticationService
) {
    @PostMapping("/signUp")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<JwtAuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.signUp(request))
    }

    @PostMapping("/signIn")
    fun signIn(@RequestBody request: SignInRequest): ResponseEntity<JwtAuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.signIn(request))
    }
}