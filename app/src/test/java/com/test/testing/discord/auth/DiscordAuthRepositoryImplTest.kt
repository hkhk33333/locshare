package com.test.testing.discord.auth

import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.TokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class DiscordAuthRepositoryImplTest {
    private lateinit var server: MockWebServer
    private lateinit var api: MySkuApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MySkuApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `success persists token and clears verifier and state`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(TokenResponse(accessToken = "tok"))))
            val context = ApplicationProvider.getApplicationContext<android.content.Context>()
            TokenStore.putCodeVerifier(context, "ver")
            TokenStore.putState(context, "st")
            val repo = DiscordAuthRepositoryImpl(context, api)
            val result = repo.exchangeAndPersistToken("code", "ver", com.test.testing.BuildConfig.DISCORD_REDIRECT_URI)
            assertEquals("tok", result.getOrNull())
            assertEquals("tok", TokenStore.get(context))
            assertTrue(TokenStore.getCodeVerifier(context).isNullOrEmpty())
            assertTrue(TokenStore.getState(context).isNullOrEmpty())
        }

    @Test
    fun `failure returns Result_failure and does not persist`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(500))
            val context = ApplicationProvider.getApplicationContext<android.content.Context>()
            val repo = DiscordAuthRepositoryImpl(context, api)
            val result = repo.exchangeAndPersistToken("code", "ver", com.test.testing.BuildConfig.DISCORD_REDIRECT_URI)
            assertTrue(result.isFailure)
            assertTrue(TokenStore.get(context).isNullOrEmpty())
        }
}
