package com.archay.busboards.data.di

import com.archay.busboards.data.remote.service.BusService
import com.archay.busboards.data.repository.BusRepository
import com.archay.busboards.data.repository.BusRepositoryImpl
import com.archay.busboards.local.BusPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBusRepository(service: BusService, preferences: BusPreferences): BusRepository = BusRepositoryImpl(service, preferences)
}