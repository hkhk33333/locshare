package com.test.testing.discord

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.test.testing.ui.theme.TestingTheme

class DiscordGateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTheme {
                DiscordPlaceholder(
                    onClose = { finish() },
                    onContinue = {
                        startActivity(android.content.Intent(this, DiscordMainActivity::class.java))
                        finish()
                    },
                )
            }
        }
    }
}

@Composable
private fun DiscordPlaceholder(
    onClose: () -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Discord system (placeholder)",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "This flow is gated by a feature flag and will be implemented incrementally.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = onClose,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(text = "Back")
        }
        Button(
            onClick = onContinue,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = "Continue")
        }
    }
}
