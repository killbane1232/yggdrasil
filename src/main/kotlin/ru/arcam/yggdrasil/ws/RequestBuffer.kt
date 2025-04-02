package ru.arcam.yggdrasil.ws

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.time.LocalDateTime

class RequestBuffer {
    private val requests = ConcurrentHashMap<String, CompletableFuture<String>>()
    private val requestTimestamps = ConcurrentHashMap<String, LocalDateTime>()
    private val timeoutMinutes = 5L

    fun addRequest(leafName: String): CompletableFuture<String> {
        val requestId = leafName
        val future = CompletableFuture<String>()
        requests[requestId] = future
        requestTimestamps[requestId] = LocalDateTime.now()
        return future
    }

    fun handleResponse(leafName: String, response: String) {
        val requestId = leafName
        requests[requestId]?.complete(response)
        requests.remove(requestId)
        requestTimestamps.remove(requestId)
    }

    fun cleanup() {
        val now = LocalDateTime.now()
        requestTimestamps.forEach { (requestId, timestamp) ->
            if (now.minusMinutes(timeoutMinutes).isAfter(timestamp)) {
                requests[requestId]?.complete("Request timeout")
                requests.remove(requestId)
                requestTimestamps.remove(requestId)
            }
        }
    }
} 