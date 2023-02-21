package com.messenger.route

import com.messenger.model.*
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class LoginRoute : ContractRoutes {

    private val loginSpec = "/login" meta {
        summary = "Login user"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }
    private val logoutSpec = "/logout" meta {
        summary = "Login user"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }

    override fun contractRoutes(): List<ContractRoute> =
        listOf(
            loginSpec bindContract POST to ::login,
            logoutSpec bindContract POST to ::logout
        )

    // Skip Auth-filter
    private fun login(): HttpHandler = { request ->
        // 1. extract login and password
        val (username, password) = authRequestLens(request)
        // 2. ToDo: validate auth data
        // 3. generate tokens
        val accessToken = generate(username)
        val refreshToken = generate(username)
        // 4. setup auth access & refresh -tokens into DB
        // 5. ToDo: setup auth access & refresh -tokens into response
        Response(OK).with(authResponseLens of AuthResponse(accessToken, refreshToken))
    }

    private fun logout(): HttpHandler = { request ->
        // 1. move into a black list the token pair
        Response(OK)
    }
}