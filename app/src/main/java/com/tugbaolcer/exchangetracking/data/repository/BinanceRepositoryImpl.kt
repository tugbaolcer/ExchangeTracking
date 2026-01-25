package com.tugbaolcer.exchangetracking.data.repository

import android.util.Log
import com.tugbaolcer.exchangetracking.data.dto.BinanceMarkPriceDto
import com.tugbaolcer.exchangetracking.di.NetworkConfig
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.domain.repository.BinanceRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BinanceRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) : BinanceRepository {

    override fun observeAllMarkPrices(): Flow<List<PriceTicker>> = callbackFlow {
        val session = client.webSocketSession {
            url("wss://fstream.binance.com/ws/!markPrice@arr@1s")
        }
        val messageJob = launch {
            try {
                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        val dtos = json.decodeFromString<List<BinanceMarkPriceDto>>(text)
                        trySend(dtos.map { it.toDomain() })
                    }
                }
            } catch (e: Exception) {
                close(e)
            }
        }
        awaitClose {
            messageJob.cancel()
            launch { session.close() }
        }
    }.flowOn(Dispatchers.IO)
}