package ru.arcam.yggdrasil.telegram.buttons.menu

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.arcam.yggdrasil.telegram.TelegramSendable
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.method.MethodSelector
import ru.arcam.yggdrasil.ws.WebSocketService

class MenuButtonView(text: String = ""): Button(text) {
    var wsService = WebSocketService.wsService

    override fun onClick(menu: CarouselMenu) : TelegramSendable? {
        val leaf = (menu as MenuSelector).leaf
        when(MenuButton.valueOf(text)) {
            MenuButton.STATUS -> {
                val message = SendMessage()
                message.chatId = menu.chatId.toString()
                message.text = wsService.processMessage(leaf.attachedBranch, "STATUS:${leaf.name}")
                return TelegramSendable(null, message)
            }
            MenuButton.STOP -> {
                val message = SendMessage()
                message.chatId = menu.chatId.toString()
                message.text = wsService.processMessage(leaf.attachedBranch, "STOP:${leaf.name}")
                return TelegramSendable(null, message)
            }
            MenuButton.START -> {
                val message = SendMessage()
                message.chatId = menu.chatId.toString()
                message.text = wsService.processMessage(leaf.attachedBranch, "START:${leaf.name}")
                return TelegramSendable(null, message)
            }
            MenuButton.METHOD -> {
                menu.nextLevel("")
                //menu.resolver.notifyUpdateMenu(menu.chatId, MethodSelector(menu.chatId, leaf!!, menu.method))
            }

            MenuButton.RESTART -> {
                val message = SendMessage()
                message.chatId = menu.chatId.toString()
                message.text = wsService.processMessage(leaf.attachedBranch, "RESTART:${leaf.name}")
                return TelegramSendable(null, message)
            }
        }
        menu.refresh()
        return null
    }

}