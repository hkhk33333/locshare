package com.test.testing.discord.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit client for the Discord + Backend system (skeleton).
 * No explicit OkHttp configuration to keep PR small and dependency-free.
 */
object ApiClient {
    // Placeholder base URL; configure via build config when wiring network calls.
    private const val BASE_URL: String = "https://example.com/"

    val api: MySkuApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MySkuApiService::class.java)
    }
}
