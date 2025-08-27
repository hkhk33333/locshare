package com.test.testing.discord.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.testing.discord.api.model.User
import com.test.testing.discord.auth.DiscordAuthRepository
import com.test.testing.discord.repo.GuildRepository
import com.test.testing.discord.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscordSettingsViewModel
    @Inject
    constructor(
        private val userRepo: UserRepository,
        private val guildRepo: GuildRepository,
        @Suppress("UnusedPrivateProperty")
        private val authRepo: DiscordAuthRepository,
    ) : ViewModel() {
        val user = MutableStateFlow<User?>(null)
        val guildCount = MutableStateFlow(0)

        // UI event for showing snackbar messages
        private val _snackbarMessage = MutableSharedFlow<String>()
        val snackbarMessage = _snackbarMessage.asSharedFlow()

        fun load() {
            viewModelScope.launch {
                user.value = userRepo.getCurrentUser().getOrNull()
                guildCount.value = guildRepo.getGuilds().getOrDefault(emptyList()).size
            }
        }

        fun logout() {
            viewModelScope.launch {
                // Logout functionality will be implemented later
                // For now, show coming soon message
                _snackbarMessage.emit("Logout coming soon")
            }
        }

        fun confirmDeleteAccount() {
            viewModelScope.launch {
                // Delete account functionality will be implemented later
                // For now, show coming soon message
                _snackbarMessage.emit("Delete account coming soon")
            }
        }
    }
