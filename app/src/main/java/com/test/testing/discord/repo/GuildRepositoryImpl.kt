package com.test.testing.discord.repo

import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.Guild

class GuildRepositoryImpl(
    private val api: MySkuApiService,
) : GuildRepository {
    override suspend fun getGuilds(): Result<List<Guild>> = runCatching { api.getGuilds() }
}
