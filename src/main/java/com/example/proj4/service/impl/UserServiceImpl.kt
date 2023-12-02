package com.example.proj4.service.impl

import com.example.proj4.entity.User
import com.example.proj4.repository.UserRepository
import com.example.proj4.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    val userRepository: UserRepository
) : UserService {
    override val userDetailsService: UserDetailsService
        get() = UserDetailsServiceImpl(userRepository)

    override val currentUser: Optional<User>
        get() {
            val auth = SecurityContextHolder.getContext().authentication

            return if(auth.isAuthenticated && auth.principal is User) {
                Optional.of(auth.principal as User)
            } else {
                Optional.empty<User>()
            }
        }

    override fun getUserByUsername(username: String): Optional<User> = userRepository.findUserByUsername(username)


    class UserDetailsServiceImpl(
        val userRepository: UserRepository
    ): UserDetailsService {
        override fun loadUserByUsername(username: String): UserDetails =
            userRepository.findUserByUsername(username).orElseThrow {UsernameNotFoundException("Пользователь не найден")}

    }
}
