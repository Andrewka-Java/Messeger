package com.messenger

import com.messenger.clients.UserClient
import com.messenger.route.AuthRoute
import com.messenger.service.AuthenticationFilterService
import com.messenger.service.AuthenticationFilterService.authenticationFilter
import org.http4k.contract.ContractRoute
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    DebuggingFilters.PrintRequest()
        .then(authenticationFilter)
        .then(getRoutes())
        .asServer(Undertow(9001))
        .start()
}

fun getRoutes(): List<ContractRoute> {
    // ToDo: mock
    val userClient = UserClient { _ -> Response(OK) }
    val putUserCredentials = AuthenticationFilterService::putUserCredentials
    val removeUserCredentials = AuthenticationFilterService::removeUserCredentials

    return AuthRoute(
        getUserByUserName = userClient::getUserByUsername,
        putUserCredentials = putUserCredentials,
        removeUserCredentials = removeUserCredentials
    ).contractRoutes()
}