package com.test.testing.discord.auth

interface DiscordAuthRepository {
    suspend fun exchangeAndPersistToken(
        code: String,
        codeVerifier: String,
        redirectUri: String,
    ): Result<String>
}
