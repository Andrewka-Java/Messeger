package com.messenger

import com.messenger.route.UserRoute
import com.messenger.service.UserService
import org.http4k.contract.ContractRoute
import org.http4k.filter.DebuggingFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    DebuggingFilters.PrintRequest()
        .then(getRoutes())
        .asServer(Undertow(9000))
        .start()
}

fun getRoutes(): List<ContractRoute> {
    return UserRoute(
        UserService::getAllUsers,
        UserService::getUserById,
        UserService::getUserByUsername,
        UserService::saveUser,
        UserService::deleteUserById
    ).contractRoutes()
}