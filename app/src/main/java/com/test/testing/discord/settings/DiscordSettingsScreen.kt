package com.test.testing.discord.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DiscordSettingsScreen(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: DiscordSettingsViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState()
    val guildCount by viewModel.guildCount.collectAsState()

    // Load data on first composition
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // Collect snackbar messages
    LaunchedEffect(viewModel) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
    ) {
        // User Header Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                // Avatar placeholder
                Box(
                    modifier =
                        Modifier
                            .size(64.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                shape = CircleShape,
                            ),
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) {
                    if (user != null) {
                        Text(
                            text =
                                user
                                    ?.duser
                                    ?.username
                                    ?.take(1)
                                    ?.uppercase() ?: "?",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    AnimatedVisibility(
                        visible = user == null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        SkeletonLine()
                    }
                    AnimatedVisibility(
                        visible = user != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Column {
                            Text(
                                text = user?.duser?.username ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Text(
                                text = "Discord User",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Discord Servers section
            SectionCard(title = "DISCORD SERVERS") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row {
                        Text(
                            text = if (guildCount == 0) "No servers connected" else "Connected to $guildCount servers",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Text(
                        text = "Server visibility controls coming soon",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Privacy section
            SectionCard(title = "PRIVACY") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row {
                        Column {
                            Text(
                                text = "Location accuracy",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Controls coming soon",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Row {
                        Column {
                            Text(
                                text = "User blocking",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Management coming soon",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            // Notifications section
            SectionCard(title = "NOTIFICATIONS") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row {
                        Column {
                            Text(
                                text = "Nearby friend alerts",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Coming soon",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Row {
                        Column {
                            Text(
                                text = "Distance preferences",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "Coming soon",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            // Account section
            SectionCard(title = "ACCOUNT") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilledTonalButton(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Logout")
                    }

                    TextButton(
                        onClick = { viewModel.confirmDeleteAccount() },
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                            ),
                    ) {
                        Text("Delete Account")
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            content()
        }
    }
}

@Composable
private fun SkeletonLine() {
    Box(
        modifier =
            Modifier
                .height(28.dp)
                .fillMaxWidth(0.6f),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                ),
        ) {}
    }
}
