package com.example.proj4.dao.response

import com.example.proj4.entity.User
import java.sql.Timestamp
import java.time.LocalDateTime

data class GetOnlineUserInfosResponse(
    val users: List<GetUserInfoResponse>
)