package com.test.testing.discord.auth

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecureTokenStoreInstrumentedTest {
    @Test
    fun writeThenRead_returnsSameToken() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SecureTokenStore.put(context, "abc")
        assertEquals("abc", SecureTokenStore.get(context))
    }

    @Test
    fun migrateFromPlain_movesAndClears_plainStore() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        TokenStore.put(context, "plain")
        SecureTokenStore.migrateFromPlain(context)
        assertEquals("plain", SecureTokenStore.get(context))
        assertTrue(TokenStore.get(context).isNullOrEmpty())
    }
}
