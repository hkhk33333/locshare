package com.test.testing.discord

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.testing.discord.auth.AuthEvents
import com.test.testing.discord.auth.DiscordLoginActivity
import com.test.testing.discord.settings.DiscordSettingsActivity
import com.test.testing.ui.theme.TestingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscordMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTheme {
                DiscordShell(
                    onOpenSettings = {
                        startActivity(Intent(this, DiscordSettingsActivity::class.java))
                    },
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                AuthEvents.authRequired.collect {
                    startActivity(Intent(this@DiscordMainActivity, DiscordLoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscordShell(onOpenSettings: () -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MySku") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Recenter functionality will be implemented later
            }) {
                Text("My location")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) },
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            DiscordMapPlaceholder()
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
