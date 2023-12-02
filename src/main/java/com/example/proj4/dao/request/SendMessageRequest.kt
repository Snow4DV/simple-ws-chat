package com.example.proj4.dao.request

import com.example.proj4.entity.User
import java.time.LocalDateTime

class SendMessageRequest() {
    lateinit var text: String

    constructor(text: String) : this() {
        this.text = text
    }
}