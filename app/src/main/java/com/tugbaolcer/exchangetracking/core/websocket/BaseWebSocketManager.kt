package com.tugbaolcer.exchangetracking.core.websocket

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


abstract class BaseWebSocketManager<DTO, Domain>(
    private val client: HttpClient,
    private val json: Json,
    private val url: String,
    private val serializer: KSerializer<DTO>
) {
    private val _state = MutableSharedFlow<SocketResource<Domain>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state = _state.asSharedFlow()

    private var scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isRunning = false

    fun connect() {
        if (isRunning) return
        isRunning = true

        scope.launch {
            while (isActive) {
                try {
                    client.webSocket(url) {
                        Log.d("WS_MANAGER", "Bağlantı Kuruldu: $url")
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                val dto = json.decodeFromString(serializer, text)
                                _state.emit(SocketResource.Connected(mapToDomain(dto)))
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WS_MANAGER", "Hata: ${e.message}. 5sn sonra tekrar denenecek.")
                    _state.emit(SocketResource.Error(e))
                    delay(5000)
                }
            }
        }
    }

    fun disconnect() {
        isRunning = false
        scope.coroutineContext.cancelChildren()
    }

    abstract fun mapToDomain(dto: DTO): Domain
}