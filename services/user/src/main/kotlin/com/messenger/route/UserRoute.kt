package com.messenger.route

import com.messenger.model.*
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path

class UserRoute(
    val getAllUsers: () -> List<User>,
    val getUserById: (UserId) -> User,
    val getUserByUsername: (Username) -> User?,
    val saveUser: (User) -> User,
    val deleteUserById: (UserId) -> Unit
) : ContractRoutes {

    private val userIdLens = Path.of("id")
    private val usernameLens = Path.of("username")

    private val getUsersSpec = "/users" meta {
        summary = "Get all users"
        returning(OK, BAD_REQUEST)
    }
    private val getUserByUsernameSpec = "/users" / usernameLens meta {
        summary = "Get a user by the username"
        returning(OK, BAD_REQUEST)
    }
    private val getUserSpec = "/users" / userIdLens meta {
        summary = "Get a user by id"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }
    private val createUserSpec = "/users" meta {
        summary = "Delete a user by id"
        returning(OK)
    }
    private val deleteUserSpec = "/users" / userIdLens meta {
        summary = "Delete a user by id"
        returning(OK, BAD_REQUEST, NOT_FOUND)
    }

    override fun contractRoutes(): List<ContractRoute> =
        listOf(
            getUsersSpec bindContract GET to ::getUsers,
            getUserByUsernameSpec bindContract GET to ::getUserByUserName,
            getUserSpec bindContract GET to ::getUser,
            createUserSpec bindContract POST to ::createUser,
            deleteUserSpec bindContract DELETE to ::deleteUser,
        )

    private fun getUsers(): HttpHandler = { _ ->
        getAllUsers().let { Response(OK).with(usersLens of it) }
    }

    private fun getUserByUserName(username: Username): HttpHandler = { _ ->
        getUserByUsername(username)?.let { Response(OK).with(userLens of it) } ?: Response(NOT_FOUND)
    }

    private fun getUser(id: UserId): HttpHandler = { _ ->
        getUserById(id).let{ Response(OK).with(userLens of it) }
    }

    private fun createUser(): HttpHandler = { request ->
        saveUser(userLens(request))
        Response(OK)
    }

    private fun deleteUser(id: UserId): HttpHandler = { _ ->
        deleteUserById(id)
        Response(OK)
    }

}
