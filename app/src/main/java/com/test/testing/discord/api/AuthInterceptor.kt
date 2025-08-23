package com.test.testing.discord.api

import android.content.Context
import com.test.testing.discord.auth.AuthEvents
import com.test.testing.discord.auth.SecureTokenStore
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds Authorization header if a token exists.
 */
class AuthInterceptor(
    private val appContext: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SecureTokenStore.get(appContext)
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
        val response = chain.proceed(request)
        if (response.code == 401) {
            // Clear token and notify listeners to re-authenticate
            SecureTokenStore.clear(appContext)
            AuthEvents.authRequired.tryEmit(Unit)
        }
        return response
    }
}
