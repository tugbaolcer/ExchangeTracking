package com.tugbaolcer.exchangetracking.presentation.components

sealed interface UIState<out T> {
    object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Error(val message: String, val throwable: Throwable? = null) : UIState<Nothing>
    object Empty : UIState<Nothing>
}