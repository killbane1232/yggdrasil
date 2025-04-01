package ru.arcam.yggdrasil.telegram

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

data class TelegramSendable(
    val sendableObject: BotApiObject?,
    val sendableMethod: SendMessage?
)
