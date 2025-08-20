package com.test.testing.discord.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Temporary token store for early wiring. Will be replaced with a Keystore-backed implementation.
 */
object TokenStore {
    private const val PREFS = "discord_auth"
    private const val KEY_TOKEN = "access_token"

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

    private fun prefs(context: Context): SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
