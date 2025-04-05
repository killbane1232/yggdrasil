package ru.arcam.yggdrasil.telegram.buttons

import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButtonView

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
        val buttonsToUse = ArrayList(buttons)
        val backButton = MenuButtonView("Back")
        backButton.callbackData = NONE
        buttonsToUse.add(backButton)
        val builder = KeyboardBuilder(text, buttonsToUse, "")
        return builder
    }

    abstract fun nextLevel(key: String)

    open fun previousLevel() {
        resolver.goBack(chatId)
    }
}