package com.messenger.route

import com.messenger.manager.UserCredentialsHashManager.Companion.hashPassword
import com.messenger.model.*
import com.messenger.model.AuthenticationFilter.Companion.getAccessTokenFromHeader
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class AuthRoute(
    val getUserByUserName: (Username) -> (User),
    val putUserCredentials: (UserId, Pair<String, String>) -> (Unit),
    val removeUserCredentials: (String) -> (Boolean),
) : ContractRoutes {

    private val loginSpec = "/login" meta {
        summary = "Login user"
        receiving(loginRequestLens to loginRequestExample)
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }
    private val logoutSpec = "/logout" meta {
        summary = "Logout user"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }

    override fun contractRoutes(): List<ContractRoute> =
        listOf(
            loginSpec bindContract POST to ::login,
            logoutSpec bindContract GET to ::logout,
        )

    // Skip Auth-filter
    private fun login(): HttpHandler = { request ->
        val (username, password) = loginRequestLens(request)
        val user = getUserByUserName(username)
        if (password.hashPassword() != user.password) {
            throw IllegalArgumentException("Provided credentials are not correct")
        }
        val accessToken = generate(username)
        val refreshToken = generate(username)
        putUserCredentials(user.userId!!, accessToken to refreshToken)

        Response(OK).with(loginResponseLens of LoginResponse(accessToken, refreshToken))
    }

    private fun logout(): HttpHandler = { request ->
        removeUserCredentials(request.getAccessTokenFromHeader())
            .let { if (it) Response(OK) else Response(NOT_FOUND) }
    }

}
