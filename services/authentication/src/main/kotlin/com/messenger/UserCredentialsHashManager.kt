package com.messenger

import com.messenger.model.Password
import java.math.BigInteger
import java.security.MessageDigest

interface UserCredentialsHashManager {
    fun Password.hashPassword(): String

    companion object : UserCredentialsHashManager {

        private const val SALT = "SALT"
        private val SHA256 = MessageDigest.getInstance("SHA-256")

        override fun Password.hashPassword() = BigInteger(1, SHA256.digest((this + SALT).toByteArray())).toString(16).padStart(32, '0')
    }

}