package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import ru.arcam.yggdrasil.telegram.TelegramSendable

abstract class Button(var text: String = "", var callbackData: String = text) {
    fun getButton(): InlineKeyboardButton {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build()
    }

    abstract fun onClick(menu: Menu)
}