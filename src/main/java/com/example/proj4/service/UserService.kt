package com.example.proj4.service

import com.example.proj4.entity.User

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*


interface UserService {
    val userDetailsService: UserDetailsService
    val currentUser: Optional<User>
}