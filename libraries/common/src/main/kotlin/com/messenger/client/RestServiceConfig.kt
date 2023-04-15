package com.messenger.client

data class RestServiceConfig(
    val serviceName: String,
    val baseUrl: String,
)

enum class RestClients(val config: RestServiceConfig) {
    USER_SERVICE(RestServiceConfig("user", "localhost:3000/user")),
    AUTHENTICATION_SERVICE(RestServiceConfig("authentication", "localhost:3000/authentication")),
    SOCKET_SERVICE(RestServiceConfig("socket", "localhost:3000/socket"))
}