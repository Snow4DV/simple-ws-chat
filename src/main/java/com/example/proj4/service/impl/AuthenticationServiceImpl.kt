package com.example.proj4.service.impl

import com.example.proj4.dao.request.SignInRequest
import com.example.proj4.dao.request.SignUpRequest
import com.example.proj4.dao.response.JwtAuthenticationResponse
import com.example.proj4.entity.Role
import com.example.proj4.entity.User
import com.example.proj4.repository.UserRepository
import com.example.proj4.service.AuthenticationService
import com.example.proj4.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthenticationServiceImpl(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) : AuthenticationService {


    override fun signUp(request: SignUpRequest): JwtAuthenticationResponse {
        val user = User(request.firstName, request.lastName, request.username, request.password, Role.USER)
        userRepository.save(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt)
    }

    override fun signIn(request: SignInRequest): JwtAuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val user = userRepository.findUserByUsername(request.username)
            .orElseThrow { IllegalArgumentException("Неверный логин или пароль") }
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt)
    }
}