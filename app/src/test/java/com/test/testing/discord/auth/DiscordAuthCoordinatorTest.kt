package com.test.testing.discord.auth

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DiscordAuthCoordinatorTest {
    @Test
    fun buildAuthUri_includesRequiredParams() {
        val uri = DiscordAuthCoordinator.buildAuthUri(codeChallenge = "abc", state = "xyz")
        val combined =
            listOf(
                uri.getQueryParameter("client_id"),
                uri.getQueryParameter("redirect_uri"),
                uri.getQueryParameter("response_type"),
                uri.getQueryParameter("scope"),
                uri.getQueryParameter("code_challenge"),
                uri.getQueryParameter("code_challenge_method"),
                uri.getQueryParameter("state"),
            ).joinToString("|")
        assertEquals("1232840493696680038|mysku://redirect|code|identify guilds|abc|S256|xyz", combined)
    }
}
