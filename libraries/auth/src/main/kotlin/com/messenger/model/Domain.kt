package com.messenger.model

import org.http4k.core.Body
import org.http4k.format.Jackson.auto

typealias UserId = String
typealias Username = String
typealias Password = String

data class User(
    val userId: UserId?,
    val username: Username,
    val password: Password,
)
data class LoginRequest(val username: Username, val password: Password)
data class LoginResponse(val accessToken: String, val responseToken: String)
val loginRequestLens = Body.auto<LoginRequest>().toLens()
val loginResponseLens = Body.auto<LoginResponse>().toLens()
val loginRequestExample = LoginRequest(
    username = "Boris.Johnson",
    password = "Boris12345",
)

val userLens = Body.auto<User>().toLens()
val usersLens = Body.auto<List<User>>().toLens()

