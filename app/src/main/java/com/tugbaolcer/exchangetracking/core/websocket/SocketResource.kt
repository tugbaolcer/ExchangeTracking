package com.tugbaolcer.exchangetracking.core.websocket

sealed class SocketResource<out T> {
    object Connecting : SocketResource<Nothing>()
    data class Connected<T>(val data: T) : SocketResource<T>()
    data class Error(val exception: Throwable) : SocketResource<Nothing>()
}