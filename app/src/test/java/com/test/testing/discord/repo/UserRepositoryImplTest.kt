package com.test.testing.discord.repo

import com.google.gson.Gson
import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.DiscordUser
import com.test.testing.discord.api.model.PrivacySettings
import com.test.testing.discord.api.model.User
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepositoryImplTest {
    private lateinit var server: MockWebServer
    private lateinit var api: MySkuApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api =
            Retrofit
                .Builder()
                .baseUrl(
                    server.url("/"),
                ).addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MySkuApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getCurrentUser parses body and returns user`() =
        runBlocking {
            val expected =
                User(
                    id = "1",
                    location = null,
                    duser = DiscordUser("1", "neo", null),
                    privacy = PrivacySettings(emptyList(), emptyList()),
                    pushToken = null,
                )
            server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(expected)))
            val repo = UserRepositoryImpl(api)
            assertEquals(
                "neo",
                repo
                    .getCurrentUser()
                    .getOrThrow()
                    .duser.username,
            )
        }
}
