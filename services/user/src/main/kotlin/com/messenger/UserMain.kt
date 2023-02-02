package com.messenger

import com.messenger.model.authenticationFilter
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    val server = DebuggingFilters.PrintRequest()
        .then(authenticationFilter)
        .then(app)
        .asServer(Undertow(9000))
        .start()
}

val app = routes(
    "/users" bind GET to {
        Response(OK).body("pong")
    }
)