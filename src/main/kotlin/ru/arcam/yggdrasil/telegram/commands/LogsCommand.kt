package ru.arcam.yggdrasil.telegram.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.TelegramBot
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector
import ru.arcam.yggdrasil.telegram.buttons.logs.LogsButton
import ru.arcam.yggdrasil.telegram.buttons.logs.LogsSelector
import ru.arcam.yggdrasil.users.UserRight

@Component(value = "/logs")
class LogsCommand(): ICommand() {
    override fun runCommand(bot: TelegramBot, chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        val menu = BranchSelector(chatId, this)
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

    override fun takeMenu(chatId: Long, resolver: StateResolver, leaf: Leaf, role: UserRight) {
        resolver.notifyUpdateMenu(chatId,
            LogsSelector(chatId,
                leaf,
                LogsButton.entries.filter{
                    it.name != "METHOD" || leaf.hooks.isNotEmpty()
                }.map{ it.button })
        )

    }
}