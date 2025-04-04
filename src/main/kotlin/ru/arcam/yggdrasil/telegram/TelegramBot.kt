package ru.arcam.yggdrasil.telegram

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector


@Component
class TelegramBot(
    private var tgBotConfig: TelegramConfiguration
) : TelegramLongPollingBot(tgBotConfig.botToken) {
    var resolver = StateResolver.resolver
    val logger = LoggerFactory.getLogger(TelegramBot::class.java)


    @PostConstruct
    fun deployBot() {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(this)
    }

    override fun getBotUsername(): String {
        return tgBotConfig.botUsername
    }

    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasCallbackQuery()) {
            val chatId = update.callbackQuery.message.chatId
            val result = resolver.peekOnClick(chatId, update.callbackQuery.data)
            if (result == null) {
                sendKeyBoard(chatId)
                return
            }
            sendMessage(chatId, result.sendableMethod!!.text)
            val lastMessage = resolver.lastMenuId[chatId]
            try {
                if (lastMessage != null) {
                    val deleter = DeleteMessage(chatId.toString(), lastMessage)
                    execute(deleter)
                    resolver.lastMenuChanged[chatId] = true
                }
            } catch(_: Throwable) {}
            sendKeyBoard(chatId)
        }
        if (update.hasMessage() && update.message.hasText()) {
            val chatId = update.message.chatId
            val messageText = update.message.text

            when(messageText) {
                "/menu" -> sendMenu(chatId)
                else ->
                    sendMessage(chatId, "Неизвестная команда")
            }
        }
    }

    fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendMenu(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        val menu = BranchSelector(chatId)
        resolver.notifyUpdateMenu(chatId, menu)
        message.replyMarkup = menu.getMenu().build()
        message.text = menu.text

        try {
            val result = execute(message)
            resolver.lastMenuId[chatId] = result.messageId
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendKeyBoard(chatId: Long, keyboard: KeyboardBuilder = resolver.peekMenu(chatId)) {
        val lastMessage = resolver.lastMenuId[chatId]
        var flag = lastMessage == null;
        try {
            if (!flag && resolver.lastMenuChanged.getOrDefault(chatId, false)) {
                val textEditor = EditMessageText(chatId.toString(),
                    lastMessage,
                    null,
                    keyboard.text,
                    null,
                    null,
                    keyboard.build(),
                    null,
                    null)
                execute(textEditor)
                resolver.lastMenuChanged[chatId] = false
            }
        } catch(ex: Throwable) {
            ex.printStackTrace()
            flag = true
        }
        if (flag) {
            val message = SendMessage()
            message.chatId = chatId.toString()
            message.replyMarkup = keyboard.build()
            message.text = keyboard.text
            try {
                val result = execute(message)
                resolver.lastMenuId[chatId] = result.messageId
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }
}