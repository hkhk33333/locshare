package com.test.testing.discord.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.test.testing.discord.api.ApiClient
import com.test.testing.discord.api.model.User
import com.test.testing.discord.repo.RepositoryFactory
import com.test.testing.discord.repo.UserRepository
import com.test.testing.discord.repo.UserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Idle : AuthState()

    data object Loading : AuthState()

    data class Success(
        val token: String,
    ) : AuthState()

    data class Error(
        val message: String,
    ) : AuthState()
}

class DiscordAuthViewModelFactory(
    private val app: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val api = ApiClient.create(app)
        val authRepo = DiscordAuthRepositoryImpl(app, api)
        val userRepo: UserRepository = UserRepositoryImpl(api)
        @Suppress("UNCHECKED_CAST")
        return DiscordAuthViewModel(authRepo, app, userRepo) as T
    }
}

class DiscordAuthViewModel(
    private val repo: DiscordAuthRepository,
    private val app: Application,
    private val userRepo: UserRepository,
) : ViewModel() {
    val state = MutableStateFlow<AuthState>(AuthState.Idle)
    val user = MutableStateFlow<User?>(null)

    constructor(repo: DiscordAuthRepository, app: Application) : this(
        repo,
        app,
        RepositoryFactory.createUserRepository(),
    )

    fun onCallback(
        code: String,
        incomingState: String?,
    ) {
        viewModelScope.launch {
            state.value = AuthState.Loading
            val savedState = TokenStore.getState(app)
            if (incomingState != null && savedState != null && incomingState != savedState) {
                state.value = AuthState.Error("State mismatch")
                return@launch
            }
            val verifier = TokenStore.getCodeVerifier(app)
            if (verifier.isNullOrEmpty()) {
                state.value = AuthState.Error("Missing verifier")
                return@launch
            }
            val res = repo.exchangeAndPersistToken(code, verifier, com.test.testing.BuildConfig.DISCORD_REDIRECT_URI)
            state.value =
                res.fold(
                    onSuccess = { AuthState.Success(it) },
                    onFailure = { AuthState.Error("Token exchange failed") },
                )
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            user.value = userRepo.getCurrentUser().getOrNull()
        }
    }
}
