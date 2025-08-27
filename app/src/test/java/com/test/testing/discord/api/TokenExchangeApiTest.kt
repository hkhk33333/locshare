package com.test.testing.discord.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.test.testing.discord.api.model.TokenRequest
import com.test.testing.discord.api.model.TokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class TokenExchangeApiTest {
    private lateinit var server: MockWebServer
    private lateinit var service: MySkuApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        service =
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
    fun `posts to token endpoint`() {
        // Given
        server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(TokenResponse(accessToken = "demo"))))

        // When
        runBlocking { service.exchangeToken(TokenRequest("code", "verifier", "mysku://redirect")) }
        val recorded = server.takeRequest()

        // Then (single behavior)
        assertEquals("/token", recorded.path)
    }

    @Test
    fun `sends snake_case request body`() {
        // Given
        server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(TokenResponse(accessToken = "demo"))))

        // When
        runBlocking { service.exchangeToken(TokenRequest("code123", "ver_456", "mysku://redirect")) }
        val body = server.takeRequest().body.readUtf8()
        val json = Gson().fromJson(body, JsonObject::class.java)

        // Then (single behavior)
        assertEquals(
            "code123|ver_456|mysku://redirect",
            "${json.get("code").asString}|${json.get("code_verifier").asString}|${json.get("redirect_uri").asString}",
        )
    }

    @Test
    fun `parses token on success`() {
        // Given
        val expected = TokenResponse(accessToken = "demo")
        server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(expected)))

        // When
        val response = runBlocking { service.exchangeToken(TokenRequest("code", "verifier", "mysku://redirect")) }

        // Then (single behavior)
        assertEquals("demo", response.accessToken)
    }
}
