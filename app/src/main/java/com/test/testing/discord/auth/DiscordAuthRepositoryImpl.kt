package com.test.testing.discord.auth

import android.content.Context
import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.TokenRequest

class DiscordAuthRepositoryImpl(
    private val context: Context,
    private val api: MySkuApiService,
) : DiscordAuthRepository {
    override suspend fun exchangeAndPersistToken(
        code: String,
        codeVerifier: String,
        redirectUri: String,
    ): Result<String> =
        runCatching {
            val resp = api.exchangeToken(TokenRequest(code, codeVerifier, redirectUri))
            require(resp.accessToken.isNotEmpty())
            TokenStore.put(context, resp.accessToken)
            TokenStore.clearCodeVerifier(context)
            TokenStore.clearState(context)
            resp.accessToken
        }
}
