package ru.arcam.yggdrasil.telegram

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuSelector
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButton
import ru.arcam.yggdrasil.ws.WebSocketService


@Component
class TelegramBot(
    private var tgBotConfig: TelegramConfiguration
) : TelegramLongPollingBot(tgBotConfig.botToken) {
    var resolver = StateResolver.resolver


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
            if (result == null)
                sendKeyBoard(chatId, resolver.peekMenu(chatId))
            else
                if (result.sendableObject != null)
                    sendKeyBoard(chatId, result.sendableObject as InlineKeyboardMarkup)
                else {
                    sendMessage(chatId, result.sendableMethod!!.text)
                    sendKeyBoard(chatId, resolver.peekMenu(chatId))
                }
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
        val menu = MenuSelector(chatId, MenuButton.entries.map{ it.button })
        resolver.notifyUpdateMenu(chatId, menu)
        message.replyMarkup = menu.getMenu()
        message.text = "Select method"

        try {
            val result = execute(message)
            resolver.lastMenuId[chatId] = result.messageId
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendKeyBoard(chatId: Long, keyboard: InlineKeyboardMarkup) {
        val lastMessage = resolver.lastMenuId[chatId]
        var flag = false;
        try {
            if (lastMessage != null) {
                val deleter = EditMessageReplyMarkup(chatId.toString(), lastMessage, null, keyboard)
                execute(deleter)
            }
        } catch(_: Throwable) {
            flag = true
        }
        if (flag) {
            val message = SendMessage()
            message.chatId = chatId.toString()
            message.replyMarkup = keyboard
            message.text = "Select"
            try {
                val result = execute(message)
                resolver.lastMenuId[chatId] = result.messageId
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }
}