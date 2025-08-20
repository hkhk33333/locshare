package com.test.testing.discord.repo

import com.test.testing.discord.api.model.Location

interface LocationRepository {
    suspend fun updateLocation(location: Location): Result<Unit>
}
