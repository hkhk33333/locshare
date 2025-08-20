package com.test.testing.discord.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit client for the Discord + Backend system with auth header support (skeleton).
 */
object ApiClient {
    // Placeholder base URL; configure via build config when wiring network calls.
    private const val BASE_URL: String = "https://example.com/"

    fun create(context: Context): MySkuApiService {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(context.applicationContext))
                .build()

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MySkuApiService::class.java)
    }
}
