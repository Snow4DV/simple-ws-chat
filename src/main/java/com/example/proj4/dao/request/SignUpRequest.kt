package com.example.proj4.dao.request

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
)