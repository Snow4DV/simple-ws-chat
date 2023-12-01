package com.example.proj4.service

import com.example.proj4.dao.request.SignInRequest
import com.example.proj4.dao.request.SignUpRequest
import com.example.proj4.dao.response.JwtAuthenticationResponse

public interface AuthenticationService {
    fun signUp(request: SignUpRequest): JwtAuthenticationResponse
    fun signIn(request: SignInRequest): JwtAuthenticationResponse
}