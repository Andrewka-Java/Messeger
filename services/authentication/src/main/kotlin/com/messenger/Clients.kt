package com.messenger

import com.messenger.model.Username
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request

class UserClient(val handler: HttpHandler) {
    fun getUserByUsername(username: Username) {
        handler(Request(GET, "/users/$username"))
    }
}
