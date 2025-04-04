package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class KeyboardBuilder(val text: String, val buttons: ArrayList<Button>, val footerText: String?) {
    private val NONE = "NONE"
    private val NEXT = "NEXT"
    private val PREVIOUS = "PREVIOUS"

    fun equals(other: KeyboardBuilder?): Boolean {
        if (other == null)
            return false
        if (other.buttons.isEmpty() && buttons.isEmpty())
            return true
        val arr = buttons.map { it.text }
        val arr2 = other.buttons.map { it.text }
        return arr == arr2 && text == other.text
    }

    fun build(): InlineKeyboardMarkup {
        val builder = InlineKeyboardMarkup.builder()
        buttons.forEach{
            builder.keyboardRow(listOf(InlineKeyboardButton.builder()
                .text(it.text)
                .callbackData(it.text)
                .build()))
        }

        if (footerText != null)
            builder.keyboardRow(
                listOf(
                    InlineKeyboardButton.builder().text("<-").callbackData(PREVIOUS).build(),
                    InlineKeyboardButton.builder().text(footerText).callbackData(NONE).build(),
                    InlineKeyboardButton.builder().text("->").callbackData(NEXT).build()
                )
            )
        return  builder.build()
    }
}