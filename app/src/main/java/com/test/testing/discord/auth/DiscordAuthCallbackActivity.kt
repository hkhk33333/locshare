package com.test.testing.discord.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

/**
 * Handles the custom scheme redirect for Discord OAuth (skeleton).
 * For now, simply returns to the login screen; token exchange is in a later PR.
 */
class DiscordAuthCallbackActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        // Extract `code` and `state` for later PR token exchange
        val code = data?.getQueryParameter("code")
        val state = data?.getQueryParameter("state")
        // Optionally validate state presence and stash code for next step
        if (!state.isNullOrEmpty()) {
            // No-op for now. In the token-exchange PR we will validate it matches TokenStore.getState(this)
        }
        if (!code.isNullOrEmpty()) {
            // In next PR: start token exchange flow
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
