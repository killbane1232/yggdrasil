package ru.arcam.yggdrasil.telegram.buttons

import ru.arcam.yggdrasil.telegram.StateResolver
import java.util.ArrayList

abstract class Menu(var chatId: Long, var buttons: List<Button>, val text: String = "Menu", ) {
    var resolver = StateResolver.resolver
    private val NONE = "NONE"

    open fun onClick(callbackData: String) {
        if (callbackData == NONE) {
            previousLevel()
            return
        }
        for (button in buttons) {
            if (button.text == callbackData) {
                button.onClick(this)
            }
        }
    }

    open fun getMenu(): KeyboardBuilder {
        val builder = KeyboardBuilder(text, ArrayList())
        return builder
    }

    abstract fun nextLevel(key: String)

    open fun previousLevel() {
        resolver.goBack(chatId)
    }
}