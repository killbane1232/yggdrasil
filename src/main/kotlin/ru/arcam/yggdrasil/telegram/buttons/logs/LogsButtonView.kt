package ru.arcam.yggdrasil.telegram.buttons.logs

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.utils.AuditLogger
import ru.arcam.yggdrasil.ws.WebSocketService
import java.util.*

class LogsButtonView(text: String = "", callback: String = text): Button(text, callback) {
    private var wsService = WebSocketService.wsService

    override fun onClick(menu: Menu) {
        val leaf = (menu as LogsSelector).leaf
        when(LogsButton.valueOf(callbackData.uppercase(Locale.getDefault()))) {
            LogsButton.TAIL -> {
                val message = "TAIL:${leaf.name}"
                AuditLogger.logWsCall(menu.chatId, message)
                wsService.processMessageAsync(leaf.attachedBranch, message) { result ->
                    menu.nextLevel(result)
                    menu.resolver.bot?.sendKeyBoard(menu.chatId)
                }
            }
            LogsButton.TAIL_N -> {
                menu.waitForMessage("Write number of rows") { num ->
                    if (num.isBlank()) {
                        menu.onClick("NONE")
                        return@waitForMessage
                    }
                    val intNum = num.toIntOrNull()
                    if (intNum == null) {
                        menu.onClick("NONE")
                        return@waitForMessage
                    }
                    val message = "TAIL_N:${leaf.name}:${intNum}"
                    AuditLogger.logWsCall(menu.chatId, message)
                    wsService.processMessageAsync(leaf.attachedBranch, message) { result ->
                        menu.nextLevel(result)
                        menu.resolver.bot?.sendKeyBoard(menu.chatId)
                    }
                }
            }
        }
    }

}