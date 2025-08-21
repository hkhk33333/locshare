package com.test.testing.discord.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.test.testing.discord.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Handles the custom scheme redirect for Discord OAuth (skeleton).
 * For now, simply returns to the login screen; token exchange is in a later PR.
 */
class DiscordAuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        // Extract `code` and `state` for later PR token exchange
        val code = data?.getQueryParameter("code")
        val state = data?.getQueryParameter("state")
        // Optionally validate state presence and stash code for next step
        val savedState = TokenStore.getState(this)
        val verifier = TokenStore.getCodeVerifier(this)

        if (state != null && savedState != null && state != savedState) {
            Toast.makeText(this, "OAuth state mismatch", Toast.LENGTH_SHORT).show()
        }

        if (!code.isNullOrEmpty() && !verifier.isNullOrEmpty()) {
            val repo = DiscordAuthRepositoryImpl(this, ApiClient.create(this))
            lifecycleScope.launch(Dispatchers.Main) {
                val result =
                    withContext(Dispatchers.IO) {
                        repo.exchangeAndPersistToken(code, verifier, com.test.testing.BuildConfig.DISCORD_REDIRECT_URI)
                    }
                result
                    .onSuccess {
                        Toast.makeText(this@DiscordAuthCallbackActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this@DiscordAuthCallbackActivity, com.test.testing.discord.DiscordMainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            },
                        )
                        finish()
                    }.onFailure {
                        Toast.makeText(this@DiscordAuthCallbackActivity, "Token exchange failed", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this@DiscordAuthCallbackActivity, DiscordLoginActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            },
                        )
                        finish()
                    }
            }
        } else {
            // Missing parameters; return to login
            startActivity(
                Intent(this, DiscordLoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                },
            )
            finish()
        }
    }
}
