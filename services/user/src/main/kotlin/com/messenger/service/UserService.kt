package com.messenger.service

import com.messenger.model.User
import com.messenger.model.UserId
import com.messenger.model.Username
import java.util.concurrent.ConcurrentHashMap

interface UserService {
    fun getAllUsers(): List<User>
    fun getUserById(userId: UserId): User
    fun getUserByUsername(username: Username): User?
    fun saveUser(user: User): User
    fun deleteUserById(userId: UserId): User?


    companion object : UserService {

        private val users: ConcurrentHashMap<UserId, User> = ConcurrentHashMap()
        private fun userIdGenerator() = (users.size + 1).toString()
        override fun getAllUsers(): List<User> = users.values as List<User>

        override fun getUserById(userId: UserId): User =
            users[userId] ?: throw NoSuchElementException("Failed to get user by id: [$userId]")

        override fun getUserByUsername(username: Username): User? =
            users.values.find { user -> user.username == username }

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

        override fun deleteUserById(userId: UserId): User? = users.remove(userId)
    }
}
