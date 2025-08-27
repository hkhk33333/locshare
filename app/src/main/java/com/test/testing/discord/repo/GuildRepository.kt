package com.test.testing.discord.repo

import com.test.testing.discord.api.model.Guild

interface GuildRepository {
    suspend fun getGuilds(): Result<List<Guild>>
}
