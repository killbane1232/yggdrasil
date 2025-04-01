package ru.arcam.yggdrasil.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramConfiguration {
    @Value("\${telegram.bot.token}")
    lateinit var botToken: String

    @Value("\${telegram.bot.username}")
    lateinit var botUsername: String
}