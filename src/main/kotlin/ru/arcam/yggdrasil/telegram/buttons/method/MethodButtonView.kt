package ru.arcam.yggdrasil.telegram.buttons.method

import ru.arcam.yggdrasil.leaf.LeafHook
import ru.arcam.yggdrasil.telegram.TelegramSendable
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.ws.WebSocketService

class MethodButtonView(private val leaf: Leaf, private val hook: LeafHook) : Button(hook.name) {
    var wsService = WebSocketService.wsService

    override fun onClick(menu: CarouselMenu) : TelegramSendable? {
        wsService.processMessage(leaf.attachedBranch, "METHOD:${leaf.name}:${hook.name}")
        return null
    }
} 