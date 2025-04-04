package ru.arcam.yggdrasil.telegram.buttons

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.TelegramSendable

abstract class CarouselMenu (var chatId: Long, var buttons: List<Button>, val text: String = "Menu") {
    var resolver = StateResolver.resolver
    val MAX_SIZE = 5
    var idx = 1
    var maxPages = 1
    private val NONE = "NONE"
    private val NEXT = "NEXT"
    private val PREVIOUS = "PREVIOUS"

    open fun getMenu(): KeyboardBuilder {
        maxPages = buttons.size / MAX_SIZE + if (buttons.size % MAX_SIZE > 0) 1 else 0
        if (maxPages == 0)
            maxPages = 1
        val buttonIdx = (idx - 1) * MAX_SIZE
        var i = 0
        val builder = KeyboardBuilder(text, arrayListOf(), "${idx}/${maxPages}")
        while (i < MAX_SIZE && i + buttonIdx < buttons.size) {
            builder.buttons.add(buttons[i + buttonIdx])
            i++
        }
        return builder
    }

    open fun onClick(callbackData: String): TelegramSendable? {
        if (callbackData == NONE) {
            previousLevel()
            return null
        }
        when (callbackData) {
            NEXT -> {
                val now = idx
                idx = (idx) % maxPages + 1
                resolver.lastMenuChanged[chatId] = idx != now
            }
            PREVIOUS -> {
                val now = idx
                idx = (idx - 2 + maxPages) % maxPages + 1
                resolver.lastMenuChanged[chatId] = idx != now
            }
            else -> for (button in buttons) {
                        if (button.text == callbackData) {
                            return button.onClick(this)
                        }
                    }
        }
        return null
    }

    fun refresh() {
        idx = 1
    }

    abstract fun nextLevel(key: String)

    fun previousLevel() {
        resolver.goBack(chatId)
    }
}