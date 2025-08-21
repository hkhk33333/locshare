package com.test.testing.discord.repo

import com.google.gson.Gson
import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.api.model.Guild
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GuildRepositoryImplTest {
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
    fun `getGuilds returns list of expected size`() =
        runBlocking {
            val payload = listOf(Guild("1", "g1", null), Guild("2", "g2", null))
            server.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(payload)))
            val repo = GuildRepositoryImpl(api)
            assertEquals(2, repo.getGuilds().getOrThrow().size)
        }
}
