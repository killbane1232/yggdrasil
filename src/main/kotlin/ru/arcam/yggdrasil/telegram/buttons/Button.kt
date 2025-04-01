package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import ru.arcam.yggdrasil.telegram.TelegramSendable

abstract class Button(var text: String = "") {
    fun getButton(): KeyboardButton {
        return KeyboardButton(text)
    }

    abstract fun onClick(menu: CarouselMenu) : TelegramSendable?
}