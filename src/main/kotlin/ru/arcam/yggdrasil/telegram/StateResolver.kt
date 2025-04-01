package ru.arcam.yggdrasil.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import java.util.*
import kotlin.collections.HashMap

@Component
class StateResolver {
    private val menuData: HashMap<Long, Stack<CarouselMenu>> = HashMap()
    val lastMenuId: HashMap<Long, Int> = HashMap()

    @Synchronized
    fun notifyUpdateMenu(chatId: Long, newMenu: CarouselMenu) {
        if (!menuData.containsKey(chatId))
            menuData[chatId] = Stack()
        menuData[chatId]!!.push(newMenu)
    }

    @Synchronized
    fun goBack(chatId: Long) {
        if (menuData.containsKey(chatId) && menuData[chatId]!!.isNotEmpty())
            menuData[chatId]!!.pop()
    }

    @Synchronized
    fun peekOnClick(chatId: Long, data: String): TelegramSendable? {
        return menuData[chatId]!!.peek().onClick(data)
    }

    @Synchronized
    fun peekMenu(chatId: Long): InlineKeyboardMarkup {
        return menuData[chatId]!!.peek().getMenu()
    }

    @Synchronized
    fun push(chatId: Long, menu: CarouselMenu) {
        if (!menuData.containsKey(chatId))
            menuData[chatId] = Stack()
        menuData[chatId]!!.push(menu)
    }

    companion object {
        val resolver = StateResolver()
    }
}