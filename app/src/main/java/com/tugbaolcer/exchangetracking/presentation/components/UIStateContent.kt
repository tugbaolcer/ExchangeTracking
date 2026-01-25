package com.tugbaolcer.exchangetracking.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun <T> UIStateContent(
    state: UIState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    successContent: @Composable (T) -> Unit
) {
    Box(modifier = modifier) {
        when (state) {
            is UIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UIState.Error -> {
                // Not: Kendi ErrorView bileşeninizi oluşturmalısınız
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = Color.Red)
                    Button(onClick = onRetry) { Text("Tekrar Dene") }
                }
                Log.d("LOG_ERROR", "error: ${state.message}")
            }
            is UIState.Success -> {
                successContent(state.data)
            }
            is UIState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Görüntülenecek veri yok.")
                }
            }
        }
    }
}