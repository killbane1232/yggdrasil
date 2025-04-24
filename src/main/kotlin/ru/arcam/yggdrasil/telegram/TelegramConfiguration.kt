package ru.arcam.yggdrasil.telegram

import org.springframework.context.annotation.Configuration
import ru.arcam.yggdrasil.utils.ConfigReader
import java.util.Properties

@Configuration
class TelegramConfiguration {
    private var properties: Properties = Properties()
    
    init {
        val configFile = ConfigReader.loadConfig("telegram.config")
        if (configFile != null) {
            properties.load(configFile.inputStream())
        }
    }

    val botToken: String
        get() = properties.getProperty("telegram.bot.token") ?: System.getenv("TELEGRAMTOKEN")?.takeIf { it.isNotBlank() } ?: throw IllegalStateException("telegram.bot.token not found in telegram.config")

    val botUsername: String
        get() = properties.getProperty("telegram.bot.username") ?: System.getenv("TELEGRAMUSERNAME")?.takeIf { it.isNotBlank() } ?: throw IllegalStateException("telegram.bot.username not found in telegram.config")
}