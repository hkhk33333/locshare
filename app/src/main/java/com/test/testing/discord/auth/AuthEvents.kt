package com.test.testing.discord.auth

import kotlinx.coroutines.flow.MutableSharedFlow

object AuthEvents {
    val authRequired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
}
