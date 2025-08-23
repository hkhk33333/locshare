package com.test.testing.discord.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.test.testing.discord.auth.AuthEvents
import com.test.testing.discord.auth.SecureTokenStore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private class FakeTerminalInterceptor(
    private val code: Int,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return Response
            .Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("")
            .body("{}".toResponseBody("application/json".toMediaType()))
            .build()
    }
}

@RunWith(RobolectricTestRunner::class)
class AuthInterceptorTest {
    @Test
    fun `adds Authorization when token present`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SecureTokenStore.put(context, "tok")

        var capturedAuth: String? = null
        val capture =
            Interceptor { chain ->
                capturedAuth = chain.request().header("Authorization")
                FakeTerminalInterceptor(200).intercept(chain)
            }

        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(capture)
                .build()
        val req = Request.Builder().url("http://example.com").build()
        client.newCall(req).execute()

        assertEquals("Bearer tok", capturedAuth)
    }

    @Test
    fun `401 clears token and emits event`() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            SecureTokenStore.put(context, "tok")
            val client =
                OkHttpClient
                    .Builder()
                    .addInterceptor(AuthInterceptor(context))
                    .addInterceptor(FakeTerminalInterceptor(401))
                    .build()

            val req = Request.Builder().url("http://example.com").build()

            var emitted = false
            val subscribed = CompletableDeferred<Unit>()
            val job =
                launch {
                    subscribed.complete(Unit)
                    withTimeout(5_000) {
                        AuthEvents.authRequired.first()
                        emitted = true
                    }
                }
            subscribed.await()
            client.newCall(req).execute()
            job.join()

            assertTrue(emitted)
            assertTrue(SecureTokenStore.get(context).isNullOrEmpty())
        }
}
