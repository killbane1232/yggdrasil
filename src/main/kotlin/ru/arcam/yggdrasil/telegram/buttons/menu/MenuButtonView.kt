package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.telegram.buttons.method.MethodSelector
import ru.arcam.yggdrasil.utils.AuditLogger
import ru.arcam.yggdrasil.ws.WebSocketService
import java.util.*

class MenuButtonView(text: String = "", callback: String = text): Button(text, callback) {
    private var wsService = WebSocketService.wsService

    override fun onClick(menu: Menu) {
        val leaf = (menu as MenuSelector).leaf
        when(MenuButton.valueOf(callbackData.uppercase(Locale.getDefault()))) {
            MenuButton.STATUS -> {
                val message = "STATUS:${leaf.name}"
                AuditLogger.logWsCall(menu.chatId, message)
                val result = wsService.processMessage(leaf.attachedBranch, message)
                menu.nextLevel(result)
            }
            MenuButton.STOP -> {
                val message = "STOP:${leaf.name}"
                AuditLogger.logWsCall(menu.chatId, message)
                val result = wsService.processMessage(leaf.attachedBranch, message)
                menu.nextLevel(result)
            }
            MenuButton.START -> {
                val message = "START:${leaf.name}"
                AuditLogger.logWsCall(menu.chatId, message)
                val result = wsService.processMessage(leaf.attachedBranch, message)
                menu.nextLevel(result)
            }
            MenuButton.METHOD -> {
                menu.resolver.notifyUpdateMenu(menu.chatId, MethodSelector(menu.chatId, leaf))
            }

            MenuButton.RESTART -> {
                val message = "RESTART:${leaf.name}"
                AuditLogger.logWsCall(menu.chatId, message)
                val result = wsService.processMessage(leaf.attachedBranch, message)
                menu.nextLevel(result)
            }
        }
    }

}