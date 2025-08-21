package com.test.testing.discord.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Base64
import androidx.core.net.toUri
import java.security.SecureRandom

/**
 * Minimal OAuth coordinator (skeleton) to kick off Discord OAuth in a browser.
 * - Launches the Discord authorize URL with PKCE params.
 * - Callback is handled by DiscordAuthCallbackActivity via a custom scheme.
 * - No network token exchange yet; safe to merge as plumbing only.
 */
object DiscordAuthCoordinator {
    private const val AUTH_BASE = "https://discord.com/api/oauth2/authorize"
    private const val RESPONSE_TYPE = "code"
    private const val SCOPE = "identify guilds"

    private val CLIENT_ID: String
        get() = com.test.testing.BuildConfig.DISCORD_CLIENT_ID

    private val REDIRECT_URI: String
        get() = com.test.testing.BuildConfig.DISCORD_REDIRECT_URI

    internal fun buildAuthUri(
        codeChallenge: String,
        state: String,
    ): Uri =
        AUTH_BASE
            .toUri()
            .buildUpon()
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .appendQueryParameter("response_type", RESPONSE_TYPE)
            .appendQueryParameter("scope", SCOPE)
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("state", state)
            .build()

    fun startLogin(activity: Activity) {
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = codeVerifier.toCodeChallenge()
        val state = generateState()

        // Persist verifier/state for callback validation and token exchange later
        TokenStore.putCodeVerifier(activity, codeVerifier)
        TokenStore.putState(activity, state)

        val uri = buildAuthUri(codeChallenge, state)

        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(intent)
    }
}

private fun generateCodeVerifier(): String {
    val secureRandom = SecureRandom()
    val randomBytes = ByteArray(32)
    secureRandom.nextBytes(randomBytes)
    return Base64.encodeToString(randomBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}

private fun String.toCodeChallenge(): String {
    val bytes = toByteArray(Charsets.US_ASCII)
    val digest =
        java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(bytes)
    return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}

private fun generateState(): String = generateCodeVerifier()
