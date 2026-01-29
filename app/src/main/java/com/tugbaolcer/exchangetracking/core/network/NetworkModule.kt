package com.tugbaolcer.exchangetracking.core.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

sealed class NetworkException : Exception() {
    data class HttpError(val code: Int) : NetworkException()
    data class Unknown(val origin: Throwable) : NetworkException()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        encodeDefaults = false
        prettyPrint = true // Loglarda JSON'un düzgün görünmesini sağlar
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        json: Json
    ): HttpClient = HttpClient(CIO) {

        expectSuccess = false

        defaultRequest {
            url(NetworkConfig.BASE_URL)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(WebSockets) {
            pingInterval = NetworkConfig.PING_INTERVAL.milliseconds
            maxFrameSize = Long.MAX_VALUE
        }

        install(HttpTimeout) {
            requestTimeoutMillis = NetworkConfig.REQUEST_TIMEOUT
            connectTimeoutMillis = NetworkConfig.CONNECT_TIMEOUT
            socketTimeoutMillis = NetworkConfig.SOCKET_TIMEOUT
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { _, response ->
                response.status.value in 500..599
            }
            retryOnExceptionIf { _, cause ->
                cause is IOException
            }
            exponentialDelay()
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HttpLogging", message)
                }
            }
            level = LogLevel.BODY
        }

        HttpResponseValidator {
            validateResponse { response ->
                val code = response.status.value
                if (code >= 400) {
                    throw NetworkException.HttpError(code)
                }
            }

            handleResponseExceptionWithRequest { cause, _ ->
                throw NetworkException.Unknown(cause)
            }
        }

        engine {
            endpoint {
                connectTimeout = NetworkConfig.CONNECT_TIMEOUT
                connectAttempts = 3
                keepAliveTime = 5_000
            }
        }
    }
}
