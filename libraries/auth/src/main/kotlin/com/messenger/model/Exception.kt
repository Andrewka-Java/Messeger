package com.messenger.model

import org.http4k.core.Status
import org.http4k.core.Status.Companion.UNAUTHORIZED

open class ResponseException(private val status: Status, message: String = status.description) : Exception(message)

class AuthenticationException(override val message: String) : ResponseException(UNAUTHORIZED, message)