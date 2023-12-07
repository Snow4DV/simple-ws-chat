package com.example.proj4.dao.request

import kotlin.properties.Delegates

class GetUserInfoRequest() {
    var userId by Delegates.notNull<Long>()
    constructor(userId: Long) : this() {
        this.userId = userId
    }
}