package ru.arcam.yggdrasil.telegram.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.TelegramBot
import ru.arcam.yggdrasil.users.UserRight
import ru.arcam.yggdrasil.utils.AuditLogger

@Component(value = "/audit")
class AuditCommand(): ICommand() {
    override fun runCommand(bot: TelegramBot, chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        
        val lastLines = AuditLogger.getLastLines(10)
        val auditText = if (lastLines.size == 1 && lastLines[0] in listOf("Audit is Empty", "Audit reading error")) {
            lastLines[0]
        } else {
            lastLines.joinToString("\n")
        }
        
        message.text = auditText

        try {
            bot.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    override fun takeMenu(chatId: Long, resolver: StateResolver, leaf: Leaf, role: UserRight) {
        // Эта команда не использует меню
    }
} 