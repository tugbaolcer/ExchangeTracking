package com.tugbaolcer.exchangetracking.presentation.binanceticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tugbaolcer.exchangetracking.domain.model.PriceTicker
import com.tugbaolcer.exchangetracking.domain.repository.BinanceRepository
import com.tugbaolcer.exchangetracking.presentation.components.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinanceTickerViewModel @Inject constructor(
    private val repository: BinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<List<PriceTicker>>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAllMarkPrices()
                .onStart {
                    _uiState.value = UIState.Loading
                }
                .catch { exception ->
                    _uiState.value = UIState.Error(exception.message ?: "Beklenmedik bir hata oluştu")
                }
                .collect { newList ->
                    if (newList.isEmpty()) {
                        _uiState.value = UIState.Empty
                    } else {
                        _uiState.value = UIState.Success(newList)
                    }
                }
        }
    }
}