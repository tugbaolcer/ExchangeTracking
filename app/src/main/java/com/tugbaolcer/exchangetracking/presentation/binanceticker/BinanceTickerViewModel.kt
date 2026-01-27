package com.tugbaolcer.exchangetracking.presentation.binanceticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.domain.usecase.GetAllMarkPricesUseCase
import com.tugbaolcer.exchangetracking.presentation.components.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BinanceTickerViewModel @Inject constructor(
    getAllMarkPricesUseCase: GetAllMarkPricesUseCase
) : ViewModel() {

    val uiState: StateFlow<UIState<Map<String, PriceTicker>>> =
        getAllMarkPricesUseCase()
            .scan(emptyMap<String, PriceTicker>()) { acc, list ->
                acc + list.associateBy { it.symbol }
            }
            .map { map ->
                if (map.isEmpty()) UIState.Empty
                else UIState.Success(map)
            }
            .onStart { emit(UIState.Loading) }
            .catch {
                emit(UIState.Error(it.message ?: "Beklenmedik hata"))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UIState.Loading
            )
}
