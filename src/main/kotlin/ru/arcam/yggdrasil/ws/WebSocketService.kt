package ru.arcam.yggdrasil.ws

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service


@Controller
class WebSocketService(private val messagingTemplate: SimpMessagingTemplate) {
    @Autowired
    private lateinit var requestBuffer: RequestBuffer

    @PostConstruct
    fun afterCreate() {
        wsService = this
    }

    @MessageMapping("/message/{username}")
    @SendTo("/topic/message/{username}")
    fun processMessage(@DestinationVariable username: String, @Payload data: String): String {
        println("Sending message to /topic/message/$username: $data")
        messagingTemplate.convertAndSend("/topic/message/$username", data)
        return requestBuffer.addRequest(username).get()
    }

    fun processMessageAsync(username: String, data: String, onResult: (String) -> Unit) {
        println("Sending message to /topic/message/$username: $data")
        messagingTemplate.convertAndSend("/topic/message/$username", data)
        Thread {
            val result = requestBuffer.addRequest(username).get()
            println("Message result: $result")
            onResult(result)
        }.start()
    }

    @MessageMapping("/callback/{username}")
    fun callbackMessage(@DestinationVariable username: String, @Payload data: String) {
        requestBuffer.handleResponse(username, data)
    }

    @Scheduled(fixedRate = 60000) // Каждую минуту
    fun cleanupRequests() {
        requestBuffer.cleanup()
    }

    companion object {
        lateinit var wsService: WebSocketService
    }
}