package com.test.testing.discord.repo

import com.test.testing.discord.api.model.User

interface UserRepository {
    suspend fun getCurrentUser(): Result<User>
}
