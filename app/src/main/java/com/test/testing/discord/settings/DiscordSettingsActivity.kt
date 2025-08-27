package com.test.testing.discord.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import com.test.testing.ui.theme.TestingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscordSettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Settings") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                    )
                                }
                            },
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { paddingValues ->
                    DiscordSettingsScreen(
                        snackbarHostState = snackbarHostState,
                        contentPadding = paddingValues,
                    )
                }
            }
        }
    }
}
