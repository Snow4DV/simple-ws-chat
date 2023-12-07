package com.example.proj4.service

import com.example.proj4.dao.response.GetUserInfoResponse
import com.example.proj4.entity.User

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*


interface UserService {
    val userDetailsService: UserDetailsService
    val currentUser: Optional<User>

    fun getUserByUsername(username: String): Optional<User>
    fun getUserById(id: Long): User?
    fun getOnlineUsersWithLastActivity(): List<GetUserInfoResponse>
    fun save(user: User): Unit
}