package com.tugbaolcer.exchangetracking.data.remote.dto

import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BinanceMarkPriceDto(
    @SerialName("s") val symbol: String,
    @SerialName("p") val markPrice: String,
    @SerialName("r") val fundingRate: String
) {
    fun toDomain() = PriceTicker(
        symbol = symbol,
        price = markPrice.toDoubleOrNull() ?: 0.0
    )
}