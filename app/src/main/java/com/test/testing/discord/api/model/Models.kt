package com.test.testing.discord.api.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    val success: Boolean,
)

data class TokenRequest(
    val code: String,
    @SerializedName("code_verifier") val codeVerifier: String,
    @SerializedName("redirect_uri") val redirectUri: String,
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
    val desiredAccuracy: Double?,
    val lastUpdated: Long,
)

data class DiscordUser(
    val id: String,
    val username: String,
    val avatar: String?,
)

data class Guild(
    val id: String,
    val name: String,
    val icon: String?,
)

data class PrivacySettings(
    val enabledGuilds: List<String>,
    val blockedUsers: List<String>,
)

data class User(
    val id: String,
    val location: Location?,
    val duser: DiscordUser,
    val privacy: PrivacySettings,
    val pushToken: String?,
    val receiveNearbyNotifications: Boolean = true,
    val allowNearbyNotifications: Boolean = true,
    val nearbyNotificationDistance: Double = 500.0,
    val allowNearbyNotificationDistance: Double = 500.0,
)
