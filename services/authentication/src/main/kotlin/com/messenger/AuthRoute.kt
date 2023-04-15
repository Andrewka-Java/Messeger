package com.messenger

import com.messenger.UserCredentialsHashManager.Companion.hashPassword
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
import java.util.concurrent.ConcurrentHashMap

class AuthRoute(
    val getUserByUserName: (Username) -> (User?),
) : ContractRoutes {

    private val loginSpec = "/login" meta {
        summary = "Login user"
        receiving(loginRequestLens to loginRequestExample)
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }
    private val logoutSpec = "/logout" meta {
        summary = "Logout user"
        receiving(logoutRequestLens to logoutRequestExample)
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }

    override fun contractRoutes(): List<ContractRoute> =
        listOf(
            loginSpec bindContract POST to ::login,
            logoutSpec bindContract POST to ::logout,
        )

    // Skip Auth-filter
    private fun login(): HttpHandler = { request ->
        val (username, password) = loginRequestLens(request)
        val user = getUserByUserName(username)
        if (password.hashPassword() != user?.password) {
            throw IllegalArgumentException("Provided credentials are not correct")
        }
        val accessToken = generate(username)
        val refreshToken = generate(username)
        userAccessRefreshTokenStore[user.userId!!] = accessToken to refreshToken

        Response(OK).with(loginResponseLens of LoginResponse(accessToken, refreshToken))
    }

    private fun logout(): HttpHandler = { request ->
        logoutRequestLens(request).let {
            userAccessRefreshTokenStore.remove(it.userId)
        }
        Response(OK)
    }

    private val userAccessRefreshTokenStore: ConcurrentHashMap<UserId, Pair<String, String>> = ConcurrentHashMap()
}
