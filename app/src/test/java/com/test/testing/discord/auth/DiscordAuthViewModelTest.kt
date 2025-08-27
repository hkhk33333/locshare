package com.test.testing.discord.auth

import android.app.Application
import com.test.testing.discord.api.model.DiscordUser
import com.test.testing.discord.api.model.PrivacySettings
import com.test.testing.discord.api.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private class FakeRepoSuccess : DiscordAuthRepository {
    override suspend fun exchangeAndPersistToken(
        code: String,
        codeVerifier: String,
        redirectUri: String,
    ): Result<String> = Result.success("tok")
}

private class FakeRepoFailure : DiscordAuthRepository {
    override suspend fun exchangeAndPersistToken(
        code: String,
        codeVerifier: String,
        redirectUri: String,
    ): Result<String> = Result.failure(IllegalStateException("boom"))
}

private class FakeUserRepo : com.test.testing.discord.repo.UserRepository {
    override suspend fun getCurrentUser(): Result<User> =
        Result.success(
            User(
                id = "1",
                location = null,
                duser = DiscordUser("1", "neo", null),
                privacy = PrivacySettings(emptyList(), emptyList()),
                pushToken = null,
            ),
        )
}

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DiscordAuthViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Error when verifier missing`() =
        runTest {
            val app =
                androidx.test.core.app.ApplicationProvider
                    .getApplicationContext<Application>()
            val vm = DiscordAuthViewModel(FakeRepoSuccess(), app)
            vm.onCallback("code", null)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(AuthState.Error("Missing verifier"), vm.state.value)
        }

    @Test
    fun `emits Error when state mismatch`() =
        runTest {
            val app =
                androidx.test.core.app.ApplicationProvider
                    .getApplicationContext<Application>()
            TokenStore.putState(app, "saved")
            val vm = DiscordAuthViewModel(FakeRepoSuccess(), app)
            vm.onCallback("code", "incoming")
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(AuthState.Error("State mismatch"), vm.state.value)
        }

    @Test
    fun `emits Error when repo fails`() =
        runTest {
            val app =
                androidx.test.core.app.ApplicationProvider
                    .getApplicationContext<Application>()
            TokenStore.putCodeVerifier(app, "ver")
            val vm = DiscordAuthViewModel(FakeRepoFailure(), app)
            vm.onCallback("code", null)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(AuthState.Error("Token exchange failed"), vm.state.value)
        }

    @Test
    fun `loadCurrentUser updates user state on success`() =
        runTest {
            val app =
                androidx.test.core.app.ApplicationProvider
                    .getApplicationContext<Application>()
            val vm = DiscordAuthViewModel(FakeRepoSuccess(), app, FakeUserRepo())
            vm.loadCurrentUser()
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(
                "neo",
                vm.user.value
                    ?.duser
                    ?.username,
            )
        }
}
