package com.test.testing.discord.di

import android.content.Context
import com.test.testing.discord.api.ApiClient
import com.test.testing.discord.api.MySkuApiService
import com.test.testing.discord.auth.DiscordAuthRepository
import com.test.testing.discord.auth.DiscordAuthRepositoryImpl
import com.test.testing.discord.repo.GuildRepository
import com.test.testing.discord.repo.GuildRepositoryImpl
import com.test.testing.discord.repo.UserRepository
import com.test.testing.discord.repo.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DiscordModule {
    @Provides
    fun provideApi(
        @ApplicationContext context: Context,
    ): MySkuApiService = ApiClient.create(context)

    @Provides
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        api: MySkuApiService,
    ): DiscordAuthRepository = DiscordAuthRepositoryImpl(context, api)

    @Provides
    fun provideUserRepository(api: MySkuApiService): UserRepository = UserRepositoryImpl(api)

    @Provides
    fun provideGuildRepository(api: MySkuApiService): GuildRepository = GuildRepositoryImpl(api)
}
