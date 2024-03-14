package com.archay.busboards.data.di

import android.content.Context
import com.archay.busboards.local.BusPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideBusPreference(@ApplicationContext context: Context): BusPreferences =
        BusPreferences(context = context)
}