package com.test.testing.discord.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.test.testing.BuildConfig
import com.test.testing.discord.DiscordMainActivity
import com.test.testing.discord.api.ApiClient
import com.test.testing.discord.api.model.TokenRequest
import com.test.testing.ui.theme.TestingTheme
import kotlinx.coroutines.launch

class DiscordLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTheme {
                DiscordLoginScreen(
                    onContinue = {
                        startActivity(Intent(this, DiscordMainActivity::class.java))
                        finish()
                    },
                )
            }
        }
    }
}

@Composable
private fun DiscordLoginScreen(onContinue: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign in with Discord",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "This is a stub screen; OAuth will be wired in later PRs.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = onContinue,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(text = "Continue with Discord")
        }

        val context = LocalContext.current
        Button(
            onClick = { DiscordAuthCoordinator.startLogin(activity = (context as androidx.activity.ComponentActivity)) },
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = "Start OAuth (browser)")
        }

        // Demo-mode quick path via backend /token (code = "demo")
        Button(
            onClick = {
                val act = (context as androidx.activity.ComponentActivity)
                act.lifecycleScope.launch {
                    val api = ApiClient.create(act)
                    val resp = api.exchangeToken(TokenRequest("demo", "demo", BuildConfig.DISCORD_REDIRECT_URI))
                    SecureTokenStore.put(act, resp.accessToken)
                    act.startActivity(Intent(act, DiscordMainActivity::class.java))
                    act.finish()
                }
            },
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = "Try Demo (no account)")
        }

        Button(
            onClick = onContinue,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = "Continue")
        }
    }
}
