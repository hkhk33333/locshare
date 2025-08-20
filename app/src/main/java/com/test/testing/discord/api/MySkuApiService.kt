package com.test.testing.discord.api

import com.test.testing.discord.api.model.BaseResponse
import com.test.testing.discord.api.model.Guild
import com.test.testing.discord.api.model.TokenRequest
import com.test.testing.discord.api.model.TokenResponse
import com.test.testing.discord.api.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit service for the Discord + Backend system.
 *
 * Notes:
 * - This is a skeleton and is not wired into UI. Safe to merge behind feature flag.
 * - Models mirror the planning document at a minimal level for compilation.
 */
interface MySkuApiService {
    @POST("token")
    suspend fun exchangeToken(
        @Body request: TokenRequest,
    ): TokenResponse

    @POST("revoke")
    suspend fun revokeToken(
        @Body request: Map<String, String>, // { token: "..." }
    ): BaseResponse

    @GET("users/me")
    suspend fun getCurrentUser(): User

    @POST("users/me")
    suspend fun updateCurrentUser(
        @Body user: User,
    ): BaseResponse

    @GET("users")
    suspend fun getVisibleUsers(): List<User>

    @GET("guilds")
    suspend fun getGuilds(): List<Guild>

    @DELETE("delete-data")
    suspend fun deleteUserData(): BaseResponse
}
