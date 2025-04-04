package ru.arcam.yggdrasil.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import java.util.*
import kotlin.collections.HashMap

@Component
class StateResolver {
    val menuData: HashMap<Long, Stack<CarouselMenu>> = HashMap()
    val lastMenuId: HashMap<Long, Int> = HashMap()
    val lastMenuChanged: HashMap<Long, Boolean> = HashMap()
    val lastMenu: HashMap<Long, KeyboardBuilder> = HashMap()

    @Synchronized
    fun notifyUpdateMenu(chatId: Long, newMenu: CarouselMenu) {
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
    fun peekOnClick(chatId: Long, data: String): TelegramSendable? {
        val result = menuData[chatId]!!.peek().onClick(data)
        lastMenuChanged[chatId] = menuData[chatId]!!.peek().getMenu().equals(lastMenu[chatId])
        return result
    }

    @Synchronized
    fun peekMenu(chatId: Long): KeyboardBuilder {
        return menuData[chatId]!!.peek().getMenu()
    }

    companion object {
        val resolver = StateResolver()
    }
}