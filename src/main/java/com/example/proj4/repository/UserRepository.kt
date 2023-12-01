package com.example.proj4.repository

import com.example.proj4.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    fun findUserByUsername(username: String) : Optional<User>
}