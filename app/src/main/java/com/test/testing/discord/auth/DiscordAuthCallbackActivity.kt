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
        // In later PR: extract `code` and `state` from data and proceed to token exchange
    }
}
