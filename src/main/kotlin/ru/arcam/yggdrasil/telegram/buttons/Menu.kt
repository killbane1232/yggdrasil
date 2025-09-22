package ru.arcam.yggdrasil.telegram.buttons

import ru.arcam.yggdrasil.telegram.StateResolver
import java.util.ArrayList
import java.util.concurrent.CompletableFuture

abstract class Menu(var chatId: Long, var buttons: List<Button>, val text: String = "Menu", ) {
    var resolver = StateResolver.resolver
    var waiter: CompletableFuture<String>? = null
    var waiterText: String? = null
    private val NONE = "NONE"

    open fun onClick(callbackData: String) {
        if (callbackData == NONE) {
            if (waiter != null)  {
                waiter!!.complete(NONE)
            }
            else {
                previousLevel()
            }
            return
        }
        for (button in buttons) {
            if (button.callbackData == callbackData) {
                if (waiter != null) {
                    waiter!!.complete(NONE)
                }
                else {
                    button.onClick(this)
                }
            }
        }
    }

    open fun onMessage(text: String): Boolean {
        if (waiter != null) {
            waiter!!.complete(text)
            return true
        }
        else
            return false
    }

    open fun getMenu(): KeyboardBuilder {
        if (waiterText != null) {
            return KeyboardBuilder(waiterText!!, ArrayList())
        }
        val builder = KeyboardBuilder(text, ArrayList())
        return builder
    }

    abstract fun nextLevel(key: String)

    open fun previousLevel() {
        resolver.goBack(chatId)
    }

    fun waitForMessage(message: String, onResult: (String) -> Unit) {
        waiter = CompletableFuture<String>()
        waiterText = message
        Thread {
            val result = waiter!!.get()
            onResult(result)
        }.start()
    }
}