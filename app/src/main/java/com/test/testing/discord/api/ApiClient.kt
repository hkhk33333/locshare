package com.test.testing.discord.api

import android.content.Context
import com.test.testing.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit client for the Discord + Backend system with auth header support (skeleton).
 */
object ApiClient {
    fun create(context: Context): MySkuApiService {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(context.applicationContext))
                .build()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.DISCORD_BACKEND_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MySkuApiService::class.java)
    }
}
