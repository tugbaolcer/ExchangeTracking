package com.tugbaolcer.exchangetracking.domain.repository

import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import kotlinx.coroutines.flow.Flow

interface BinanceRepository {
    fun observeAllMarkPrices(): Flow<List<PriceTicker>>
}