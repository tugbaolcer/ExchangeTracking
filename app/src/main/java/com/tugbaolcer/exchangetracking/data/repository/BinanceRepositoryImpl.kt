package com.tugbaolcer.exchangetracking.data.repository

import com.tugbaolcer.exchangetracking.core.websocket.SocketResource
import com.tugbaolcer.exchangetracking.data.remote.websocket.MarkPriceSocketManager
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.domain.repository.BinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class BinanceRepositoryImpl @Inject constructor(
    private val markPriceSocketManager: MarkPriceSocketManager
) : BinanceRepository {

    override fun observeAllMarkPrices(): Flow<SocketResource<List<PriceTicker>>> {
        return markPriceSocketManager.state
            .onStart { markPriceSocketManager.connect() }
            .onCompletion { markPriceSocketManager.disconnect() }
    }
}