package com.test.testing.discord.repo

import com.test.testing.discord.api.model.Location
import com.test.testing.discord.api.model.User

/**
 * Factory and stub implementations to keep PR small and non-breaking.
 * These stubs return Result.success without side effects.
 */
object RepositoryFactory {
    fun createUserRepository(): UserRepository = StubUserRepository

    fun createGuildRepository(): GuildRepository = StubGuildRepository

    fun createLocationRepository(): LocationRepository = StubLocationRepository
}

private object StubUserRepository : UserRepository {
    override suspend fun getCurrentUser(): Result<User> = Result.failure(UnsupportedOperationException("Not implemented"))
}

private object StubGuildRepository : GuildRepository {
    override suspend fun getGuilds() = Result.success(emptyList<com.test.testing.discord.api.model.Guild>())
}

private object StubLocationRepository : LocationRepository {
    override suspend fun updateLocation(location: Location): Result<Unit> = Result.success(Unit)
}
