package com.test.testing.discord.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Temporary token store for early wiring. Will be replaced with a Keystore-backed implementation.
 */
object TokenStore {
    private const val PREFS = "discord_auth"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_CODE_VERIFIER = "code_verifier"
    private const val KEY_STATE = "oauth_state"

    fun put(
        context: Context,
        token: String?,
    ) {
        val prefs = prefs(context)
        if (token == null) {
            prefs.edit().remove(KEY_TOKEN).apply()
        } else {
            prefs.edit().putString(KEY_TOKEN, token).apply()
        }
    }

    fun get(context: Context): String? = prefs(context).getString(KEY_TOKEN, null)

    fun putCodeVerifier(
        context: Context,
        codeVerifier: String,
    ) {
        prefs(context).edit().putString(KEY_CODE_VERIFIER, codeVerifier).apply()
    }

    fun getCodeVerifier(context: Context): String? = prefs(context).getString(KEY_CODE_VERIFIER, null)

    fun clearCodeVerifier(context: Context) {
        prefs(context).edit().remove(KEY_CODE_VERIFIER).apply()
    }

    fun putState(
        context: Context,
        state: String,
    ) {
        prefs(context).edit().putString(KEY_STATE, state).apply()
    }

    fun getState(context: Context): String? = prefs(context).getString(KEY_STATE, null)

    fun clearState(context: Context) {
        prefs(context).edit().remove(KEY_STATE).apply()
    }

    private fun prefs(context: Context): SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
