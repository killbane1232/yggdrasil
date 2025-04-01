package ru.arcam.yggdrasil.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.File
import java.util.Properties

@Configuration
class TelegramConfiguration {
    private var properties: Properties = Properties()
    
    init {
        val configFile = File("telegram.config")
        if (configFile.exists()) {
            properties.load(configFile.inputStream())
        }
    }

    val botToken: String
        get() = properties.getProperty("telegram.bot.token") ?: throw IllegalStateException("telegram.bot.token not found in telegram.config")

    val botUsername: String
        get() = properties.getProperty("telegram.bot.username") ?: throw IllegalStateException("telegram.bot.username not found in telegram.config")
}