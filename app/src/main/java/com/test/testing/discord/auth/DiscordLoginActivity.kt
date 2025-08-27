package com.test.testing.discord.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
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
                DiscordLoginScreen()
            }
        }
    }
}

@Composable
private fun DiscordLoginScreen() {
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
            text = "Opens Discord to sign in.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
        )

        val context = LocalContext.current
        Button(
            onClick = { DiscordAuthCoordinator.startLogin(activity = (context as ComponentActivity)) },
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(text = "Continue with Discord")
        }

        // Debug-only demo link (text)
        if (BuildConfig.DISCORD_DEMO_MODE) {
            Text(
                text = "Try demo (debug)",
                modifier =
                    Modifier
                        .padding(top = 12.dp)
                        .clickable {
                            val act = (context as ComponentActivity)
                            act.lifecycleScope.launch {
                                val api = ApiClient.create(act)
                                val resp =
                                    api.exchangeToken(
                                        TokenRequest(
                                            "demo",
                                            "demo",
                                            BuildConfig.DISCORD_REDIRECT_URI,
                                        ),
                                    )
                                SecureTokenStore.put(act, resp.accessToken)
                                act.startActivity(Intent(act, DiscordMainActivity::class.java))
                                act.finish()
                            }
                        },
            )
        }
    }
}
