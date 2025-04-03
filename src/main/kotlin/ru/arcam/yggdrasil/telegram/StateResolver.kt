package ru.arcam.yggdrasil.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import java.util.*
import kotlin.collections.HashMap

@Component
class StateResolver {
    val menuData: HashMap<Long, Stack<CarouselMenu>> = HashMap()
    val lastMenuId: HashMap<Long, Int> = HashMap()
    val lastMenuChanged: HashMap<Long, Boolean> = HashMap()

    @Synchronized
    fun notifyUpdateMenu(chatId: Long, newMenu: CarouselMenu) {
        if (!menuData.containsKey(chatId))
            menuData[chatId] = Stack()
        menuData[chatId]!!.push(newMenu)
        lastMenuChanged[chatId] = true
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
        return menuData[chatId]!!.peek().onClick(data)
    }

    @Synchronized
    fun peekMenu(chatId: Long): InlineKeyboardMarkup {
        return menuData[chatId]!!.peek().getMenu()
    }

    companion object {
        val resolver = StateResolver()
    }
}