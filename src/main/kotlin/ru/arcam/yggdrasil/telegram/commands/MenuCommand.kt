package ru.arcam.yggdrasil.telegram.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.arcam.yggdrasil.telegram.TelegramBot
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector

@Component(value = "/menu")
class MenuCommand(): ICommand() {
    override fun runCommand(bot: TelegramBot, chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        val menu = BranchSelector(chatId)
        bot.resolver.notifyUpdateMenu(chatId, menu)
        message.replyMarkup = menu.getMenu().build(false)
        message.text = menu.text

        try {
            val result = bot.execute(message)
            bot.resolver.lastMenuId[chatId] = result.messageId
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}