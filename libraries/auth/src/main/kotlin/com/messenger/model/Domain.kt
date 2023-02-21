package com.messenger.model

import org.http4k.core.Body
import org.http4k.format.Jackson.auto

data class User(val username: String)
data class AuthRequest(val username: String, val password: String)
data class AuthResponse(val accessToken: String, val responseToken: String)
val authRequestLens = Body.auto<AuthRequest>().toLens()
val authResponseLens = Body.auto<AuthResponse>().toLens()