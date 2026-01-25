package com.tugbaolcer.exchangetracking.domain.usecase

import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.domain.repository.BinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPriceStreamUseCase @Inject constructor(
    private val repository: BinanceRepository
) {
    operator fun invoke(): Flow<List<PriceTicker>> =
        repository.observeAllMarkPrices()
}