package com.tugbaolcer.exchangetracking.presentation.binanceticker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.presentation.components.UIStateContent
import kotlinx.coroutines.delay


@Composable
fun BinanceTickerScreen(
    modifier: Modifier = Modifier,
    viewModel: BinanceTickerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    UIStateContent(state = state) { tickerMap ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tickerMap.values.toList(),
                key = { it.symbol }
            ) { ticker ->
                PriceCard(ticker)
            }
        }
    }
}

@Composable
fun PriceCard(ticker: PriceTicker) {
    var lastPrice by remember { mutableDoubleStateOf(ticker.price) }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            ticker.price > lastPrice -> Color.Green.copy(alpha = 0.2f)
            ticker.price < lastPrice -> Color.Red.copy(alpha = 0.2f)
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 300),
        label = "PriceAnim"
    )

    LaunchedEffect(ticker.price) {
        delay(500)
        lastPrice = ticker.price
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ticker.symbol,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = String.format("%.2f", ticker.price),
                color = if (ticker.price >= lastPrice) Color.Green else Color.Red,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}