package ru.arcam.yggdrasil.telegram

import org.springframework.stereotype.Component
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.Menu
import java.util.*
import kotlin.collections.HashMap

@Component
class StateResolver {
    val menuData: HashMap<Long, Stack<Menu>> = HashMap()
    val lastMenuId: HashMap<Long, Int> = HashMap()
    val lastMenuChanged: HashMap<Long, Boolean> = HashMap()

    @Synchronized
    fun notifyUpdateMenu(chatId: Long, newMenu: Menu) {
        if (!menuData.containsKey(chatId))
            menuData[chatId] = Stack()
        lastMenuChanged[chatId] = true
        menuData[chatId]!!.push(newMenu)
    }

    @Synchronized
    fun goBack(chatId: Long) {
        if (menuData.containsKey(chatId) && menuData[chatId]!!.size > 1) {
            menuData[chatId]!!.pop()
            lastMenuChanged[chatId] = true
        }
    }

    @Synchronized
    fun peekOnClick(chatId: Long, data: String) {
        val previous = peekMenu(chatId)
        menuData[chatId]!!.peek().onClick(data)
        lastMenuChanged[chatId] = !previous.equals(peekMenu(chatId))
    }

    @Synchronized
    fun peekOnMessage(chatId: Long, data: String): Boolean {
        val previous = peekMenu(chatId)
        if (menuData[chatId] != null) {
            menuData[chatId]!!.peek().onMessage(data)
            lastMenuChanged[chatId] = !previous.equals(peekMenu(chatId))
            return true
        }
        return false
    }

    @Synchronized
    fun peekMenu(chatId: Long): KeyboardBuilder {
        return menuData[chatId]!!.peek().getMenu()
    }

    companion object {
        val resolver = StateResolver()
    }
}