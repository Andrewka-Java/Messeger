package com.messenger.route

import com.messenger.model.ContractRoutes
import com.messenger.model.Username
import com.messenger.model.userLens
import com.messenger.service.UserService
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path

class UserRoute(val userService: UserService) : ContractRoutes {

    private val userIdLens = Path.of("id")
    private val usernameLens = Path.of("username")

    private val usersSpec = "/users" meta {
        summary = "Get all users"
        returning(OK, BAD_REQUEST)
    }
    private val userByUsernameSpec = "/users" / usernameLens meta {
        summary = "Get a user by the username"
        returning(OK, BAD_REQUEST)
    }
    private val userSpec = "/users" / userIdLens meta {
        summary = "Get a user by id"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }
    private val deleteUserSpec = "/users" / userIdLens meta {
        summary = "Delete a user by id"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }

    override fun contractRoutes(): List<ContractRoute> =
        listOf(
            usersSpec bindContract GET to ::getUsers,
            userByUsernameSpec bindContract GET to ::getUserByUserName,
            userSpec bindContract GET to ::getUser,
            deleteUserSpec bindContract DELETE to ::deleteUser,
        )

    private fun getUsers(): HttpHandler = { request ->
        Response(OK)
    }

    private fun getUserByUserName(username: Username): HttpHandler = { request ->
        userService.getUserByUsername(username).let {
            if (it == null) Response(NOT_FOUND) else Response(OK).with(userLens of it)
        }
    }

    private fun getUser(id: String): HttpHandler = { request ->
        Response(OK)
    }

    private fun deleteUser(id: String): HttpHandler = { request ->
        Response(OK)
    }

}
