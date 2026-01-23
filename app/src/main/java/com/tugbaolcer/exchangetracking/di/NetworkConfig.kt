package com.tugbaolcer.exchangetracking.di

object NetworkConfig {
    const val BASE_URL = "wss://fstream.binance.com"
    const val CONNECT_TIMEOUT = 15_000L
    const val REQUEST_TIMEOUT  = 15_000L
    const val SOCKET_TIMEOUT = 15_000L
    const val PING_INTERVAL = 15_000L
}