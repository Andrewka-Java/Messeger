package com.messenger.model

import org.http4k.core.Request


interface AuthenticationFilter {

    fun authenticate(accessToken: String): Boolean
    fun putUserCredentials(userId: UserId, accessRefreshTokenPair: Pair<String, String>)
    fun removeUserCredentials(accessToken: String): Boolean

    companion object {
        fun Request.getAccessTokenFromHeader() = header("Authorization") ?: throw AuthenticationException("Failed to authenticate the user")
    }

}


