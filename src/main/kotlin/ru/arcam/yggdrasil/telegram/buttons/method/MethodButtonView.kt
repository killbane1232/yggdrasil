package ru.arcam.yggdrasil.telegram.buttons.method

import ru.arcam.yggdrasil.leaf.LeafHook
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.utils.AuditLogger
import ru.arcam.yggdrasil.ws.WebSocketService

class MethodButtonView(private val leaf: Leaf, private val hook: LeafHook) : Button(hook.name) {
    var wsService = WebSocketService.wsService

    override fun onClick(menu: Menu) {
        val message = "METHOD:${leaf.name}:${hook.name}"
        AuditLogger.logWsCall(menu.chatId, message)
        wsService.processMessage(leaf.attachedBranch, message)
    }
} 