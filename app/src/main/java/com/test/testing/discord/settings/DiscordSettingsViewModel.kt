package com.test.testing.discord.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.testing.discord.api.model.User
import com.test.testing.discord.repo.GuildRepository
import com.test.testing.discord.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscordSettingsViewModel
    @Inject
    constructor(
        private val userRepo: UserRepository,
        private val guildRepo: GuildRepository,
    ) : ViewModel() {
        val user = MutableStateFlow<User?>(null)
        val guildCount = MutableStateFlow(0)

        fun load() {
            viewModelScope.launch {
                user.value = userRepo.getCurrentUser().getOrNull()
                guildCount.value = guildRepo.getGuilds().getOrDefault(emptyList()).size
            }
        }
    }
