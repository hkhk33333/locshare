package com.test.testing.discord.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecureTokenStore {
    private const val PREFS = "secure_discord_auth"
    private const val KEY_TOKEN = "access_token"

    private fun prefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREFS,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    fun put(
        context: Context,
        token: String?,
    ) {
        val p = prefs(context)
        if (token.isNullOrEmpty()) {
            p.edit().remove(KEY_TOKEN).apply()
        } else {
            p.edit().putString(KEY_TOKEN, token).apply()
        }
    }

    fun get(context: Context): String? = prefs(context).getString(KEY_TOKEN, null)

    fun clear(context: Context) {
        prefs(context).edit().remove(KEY_TOKEN).apply()
    }

    fun migrateFromPlain(context: Context) {
        val plain = TokenStore.get(context)
        if (!plain.isNullOrEmpty()) {
            put(context, plain)
            TokenStore.put(context, null)
        }
    }
}
