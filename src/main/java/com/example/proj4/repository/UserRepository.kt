package com.example.proj4.repository

import com.example.proj4.dao.response.GetUserInfoResponse
import com.example.proj4.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    fun findUserByUsername(username: String) : Optional<User>
    @Query("SELECT NEW com.example.proj4.dao.response.GetUserInfoResponse(u, MAX(m.timestamp)) " +
            "FROM User u " +
            "LEFT JOIN Message m ON u.id = m.sender.id " +
            "WHERE u.online = TRUE " +
            "GROUP BY u.id " +
            "ORDER BY MAX(m.timestamp) DESC")
    fun getOnlineUsers(): List<GetUserInfoResponse>
}