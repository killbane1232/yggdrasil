package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.TelegramSendable

abstract class CarouselMenu (var chatId: Long, var buttons: List<Button>, var method: String = "") {
    var resolver = StateResolver.resolver
    val MAX_SIZE = 5
    var idx = 1
    var maxPages = 1
    private val NONE = "NONE"
    private val NEXT = "NEXT"
    private val PREVIOUS = "PREVIOUS"


    open fun getMenu(): InlineKeyboardMarkup {
        val builder = InlineKeyboardMarkup.builder()
        maxPages = buttons.size / MAX_SIZE + if (buttons.size % MAX_SIZE > 0) 1 else 0
        val buttonIdx = (idx - 1) * MAX_SIZE
        var i = 0
        while (i < MAX_SIZE && i + buttonIdx < buttons.size) {
            builder.keyboardRow(listOf(InlineKeyboardButton.builder()
                .text(buttons[i + buttonIdx].text)
                .callbackData("${buttons[i + buttonIdx].text}.${method}")
                .build()))
            i++
        }
        builder.keyboardRow(
            listOf(
                InlineKeyboardButton.builder().text("<-").callbackData(PREVIOUS).build(),
                InlineKeyboardButton.builder().text("${idx}/${maxPages}").callbackData(NONE).build(),
                InlineKeyboardButton.builder().text("->").callbackData(NEXT).build()
            )
        )
        return builder.build()
    }

    open fun onClick(callbackData: String): TelegramSendable? {
        if (callbackData == NONE) {
            previousLevel()
            return TelegramSendable(resolver.peekMenu(chatId), null)
        }
        when (callbackData) {
            NEXT -> idx = (idx + 1) % maxPages + 1
            PREVIOUS -> idx = (idx - 1 + maxPages) % maxPages + 1
            else -> if (callbackData.endsWith(method)) {
                val name = callbackData.substring(0, callbackData.lastIndexOf(method) - 1)
                for (button in buttons) {
                    if (button.text == name) {
                        return button.onClick(this)
                    }
                }
            }
        }
        return TelegramSendable(getMenu(), null)
    }

    fun refresh() {
        idx = 1
    }

    abstract fun nextLevel(key: String)

    fun previousLevel() {
        resolver.goBack(chatId)
    }
}