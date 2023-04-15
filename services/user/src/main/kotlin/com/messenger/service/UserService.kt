package com.messenger.service

import com.messenger.model.Password
import com.messenger.model.User
import com.messenger.model.UserId
import com.messenger.model.Username
import java.util.concurrent.ConcurrentHashMap

interface UserService {
    fun getUserById(userId: UserId): User
    fun getUserByUsername(username: Username): User?
    fun validatePassword(username: Username, password: Password): Boolean
    fun saveUser(user: User): User
}

class UserServiceImpl(
    val hashPassword: Password.() -> String,
) : UserService {
    override fun getUserById(userId: UserId): User =
        users[userId] ?: throw NoSuchElementException("Failed to get user by id: [$userId]")

    override fun getUserByUsername(username: Username): User? =
        users.values.find { user -> user.username == username }

    override fun validatePassword(username: Username, password: Password): Boolean =
        hashPassword(password) == getUserByUsername(username)?.password

    override fun saveUser(user: User): User =
        when (user.userId) {
            null -> {
                userIdGenerator().let {
                    users[it] = user.copy(userId = it)
                    users[it]
                }!!
            }
            else -> {
                users[user.userId!!] = user
                user
            }
        }

    companion object {
        private val users: ConcurrentHashMap<UserId, User> = ConcurrentHashMap()
        fun userIdGenerator() = (users.size + 1).toString()
    }
}
