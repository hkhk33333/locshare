package com.test.testing.discord.api

import android.content.Context
import com.test.testing.discord.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds Authorization header if a token exists (skeleton).
 */
class AuthInterceptor(
    private val appContext: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenStore.get(appContext)
        val request =
            if (!token.isNullOrEmpty()) {
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
        return chain.proceed(request)
    }
}
