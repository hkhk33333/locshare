package com.test.testing.discord.settings

import com.test.testing.discord.api.model.DiscordUser
import com.test.testing.discord.api.model.PrivacySettings
import com.test.testing.discord.api.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private class FakeUserRepo : com.test.testing.discord.repo.UserRepository {
    override suspend fun getCurrentUser(): Result<User> =
        Result.success(
            User("1", null, DiscordUser("1", "neo", null), PrivacySettings(emptyList(), emptyList()), null),
        )
}

private class FakeGuildRepo : com.test.testing.discord.repo.GuildRepository {
    override suspend fun getGuilds(): Result<List<com.test.testing.discord.api.model.Guild>> =
        Result.success(
            listOf(
                com.test.testing.discord.api.model
                    .Guild("1", "g1", null),
                com.test.testing.discord.api.model
                    .Guild("2", "g2", null),
            ),
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
class DiscordSettingsViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun load_updates_user_and_count() =
        runTest {
            val vm = DiscordSettingsViewModel(FakeUserRepo(), FakeGuildRepo())
            vm.load()
            advanceUntilIdle()
            assertEquals(
                "neo",
                vm.user.value
                    ?.duser
                    ?.username,
            )
            assertEquals(2, vm.guildCount.value)
        }
}
