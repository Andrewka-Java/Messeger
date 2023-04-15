package com.messenger.service

import com.messenger.model.AuthenticationException
import com.messenger.model.AuthenticationFilter
import com.messenger.model.AuthenticationFilter.Companion.getAccessTokenFromHeader
import com.messenger.model.UserId
import com.messenger.model.isExpired
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import java.util.concurrent.ConcurrentHashMap

object AuthenticationFilterService : AuthenticationFilter {

    private val userAccessRefreshTokenStore: ConcurrentHashMap<UserId, Pair<String, String>> = ConcurrentHashMap()

    val authenticationFilter = Filter { next ->
        { request ->
            try {
                next(request.authenticate())
            } catch (ex: AuthenticationException) {
                Response(Status.FORBIDDEN).body(ex.message)
            }
        }
    }

    override fun authenticate(accessToken: String): Boolean =
        userAccessRefreshTokenStore.filterValues { (key, _) -> key == accessToken }.isNotEmpty()

    override fun putUserCredentials(userId: UserId, accessRefreshTokenPair: Pair<String, String>) {
        userAccessRefreshTokenStore[userId] = accessRefreshTokenPair
    }

    override fun removeUserCredentials(accessToken: String): Boolean =
        userAccessRefreshTokenStore.entries.removeIf { it.value.first == accessToken }

    private fun Request.authenticate(): Request {
        val accessToken = getAccessTokenFromHeader()
        with(accessToken) {
            if (!startsWith("Bearer ") || isExpired(accessToken)) {
                throw AuthenticationException("Failed to authenticate the user")
            }
        }
        if (!authenticate(accessToken)) throw AuthenticationException("Access token isn't valid")
        return this
    }
}

