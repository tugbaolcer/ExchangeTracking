package com.tugbaolcer.exchangetracking.di

import com.tugbaolcer.exchangetracking.data.repository.BinanceRepositoryImpl
import com.tugbaolcer.exchangetracking.domain.repository.BinanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBinanceRepository(
        binanceRepositoryImpl: BinanceRepositoryImpl
    ): BinanceRepository
}