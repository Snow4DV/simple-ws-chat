package com.example.proj4.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity
@Table(name = "_user")
class User() : UserDetails{
    lateinit var firstName: String
    lateinit var lastName: String

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null


    @Column(unique = true)
    private lateinit var username: String
    private lateinit var password: String

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER




    constructor(firstName: String, lastName: String, username: String, password: String, role: Role) : this() {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
        this.role = role
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}




