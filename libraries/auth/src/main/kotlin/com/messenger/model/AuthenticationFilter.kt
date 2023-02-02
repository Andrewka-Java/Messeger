package com.messenger.model

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

val authenticationFilter = Filter { next ->
    { request ->
        try {
            next(request.authenticate())
        } catch (ex: AuthenticationException) {
            Response(Status.FORBIDDEN).body(ex.message)
        }
    }
}

private fun Request.authenticate(): Request {
    val authToken = header("Authorization") ?: throw AuthenticationException("Failed to authenticate the user")
    with(authToken) {
        if (!startsWith("Bearer ") || isExpired(authToken)) {
            throw AuthenticationException("Failed to authenticate the user")
        }
    }
    return this
}
