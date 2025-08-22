package com.test.testing.discord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.testing.discord.settings.DiscordSettingsViewModel
import com.test.testing.ui.theme.TestingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscordMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTheme {
                DiscordShell()
            }
        }
    }
}

private enum class DiscordTab { MAP, SETTINGS }

@Composable
private fun DiscordShell() {
    val (currentTab, setCurrentTab) = remember { mutableStateOf(DiscordTab.MAP) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == DiscordTab.MAP,
                    onClick = { setCurrentTab(DiscordTab.MAP) },
                    label = { Text("Map") },
                    icon = { },
                )
                NavigationBarItem(
                    selected = currentTab == DiscordTab.SETTINGS,
                    onClick = { setCurrentTab(DiscordTab.SETTINGS) },
                    label = { Text("Settings") },
                    icon = { },
                )
            }
        },
    ) { innerPadding ->
        DiscordContent(innerPadding = innerPadding, tab = currentTab)
    }
}

@Composable
private fun DiscordContent(
    innerPadding: PaddingValues,
    tab: DiscordTab,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
    ) {
        when (tab) {
            DiscordTab.MAP -> DiscordMapPlaceholder()
            DiscordTab.SETTINGS -> DiscordSettingsScreen()
        }
    }
}

@Composable
private fun DiscordMapPlaceholder() {
    Text(
        text = "Discord Map (placeholder)",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun DiscordSettingsScreen() {
    val vm: DiscordSettingsViewModel = hiltViewModel()
    val userState = vm.user.collectAsState()
    val countState = vm.guildCount.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Discord Settings (placeholder)",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Greeting(userState.value)
        Text(text = "Guilds: ${countState.value}")
    }
}

@Composable
private fun Greeting(user: com.test.testing.discord.api.model.User?) {
    if (user != null) {
        Text(
            text = "Hello, ${user.duser.username}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
        )
    }
}
