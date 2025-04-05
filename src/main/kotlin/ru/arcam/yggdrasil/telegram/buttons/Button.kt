package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

open class Button(val text: String = "Text", val callbackData: String = text) {
    fun getButton(): InlineKeyboardButton {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build()
    }

    open fun onClick(menu: Menu) {}
}