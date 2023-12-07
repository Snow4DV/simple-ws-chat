package com.example.proj4.dao.response

import com.example.proj4.entity.User
import java.sql.Timestamp
import java.time.LocalDateTime

data class GetUserInfoResponse(
    val user: User,
    val lastActivity: LocalDateTime?
)