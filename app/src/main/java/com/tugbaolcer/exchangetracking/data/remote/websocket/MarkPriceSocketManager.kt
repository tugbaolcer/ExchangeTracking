package com.tugbaolcer.exchangetracking.data.remote.websocket

import com.tugbaolcer.exchangetracking.core.websocket.BaseWebSocketManager
import com.tugbaolcer.exchangetracking.data.remote.dto.BinanceMarkPriceDto
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import io.ktor.client.HttpClient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkPriceSocketManager @Inject constructor(
    client: HttpClient,
    json: Json
) : BaseWebSocketManager<List<BinanceMarkPriceDto>, List<PriceTicker>>(
    client = client,
    json = json,
    url = "wss://fstream.binance.com/ws/!markPrice@arr@1s",
    serializer = ListSerializer(BinanceMarkPriceDto.serializer())
) {
    override fun mapToDomain(dto: List<BinanceMarkPriceDto>): List<PriceTicker> =
        dto.map { it.toDomain() }
}