package com.test.testing.discord.repo

import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.User

class UserRepositoryImpl(
    private val api: MySkuApiService,
) : UserRepository {
    override suspend fun getCurrentUser(): Result<User> = runCatching { api.getCurrentUser() }
}
