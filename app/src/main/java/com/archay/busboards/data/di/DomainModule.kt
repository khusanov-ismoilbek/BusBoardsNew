package com.archay.busboards.data.di

import com.archay.busboards.data.repository.BusRepository
import com.archay.busboards.domain.BusUseCase
import com.archay.busboards.domain.BusUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideBusUseCase(repository: BusRepository): BusUseCase = BusUseCaseImpl(repository)

}