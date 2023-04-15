package com.messenger.clients

import com.messenger.model.AuthenticationException
import com.messenger.model.User
import com.messenger.model.Username
import com.messenger.model.userLens
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status

class UserClient(val handler: HttpHandler) {
    fun getUserByUsername(username: Username): User {
        handler(Request(GET, "/users/$username")).let {
            if (it.status == Status.NOT_FOUND) throw AuthenticationException("Failed to find user with name [${username}]")
            return userLens(it)
        }
    }
}
