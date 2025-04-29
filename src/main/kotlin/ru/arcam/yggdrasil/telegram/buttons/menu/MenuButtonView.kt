package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.ws.WebSocketService
import java.util.*

class MenuButtonView(text: String = "", callback: String = text): Button(text, callback) {
    private var wsService = WebSocketService.wsService

    override fun onClick(menu: Menu) {
        val leaf = (menu as MenuSelector).leaf
        when(MenuButton.valueOf(text.uppercase(Locale.getDefault()))) {
            MenuButton.STATUS -> {
                val result = wsService.processMessage(leaf.attachedBranch, "STATUS:${leaf.name}")
                menu.nextLevel(result)
            }
            MenuButton.STOP -> {
                val result = wsService.processMessage(leaf.attachedBranch, "STOP:${leaf.name}")
                menu.nextLevel(result)
            }
            MenuButton.START -> {
                val result = wsService.processMessage(leaf.attachedBranch, "START:${leaf.name}")
                menu.nextLevel(result)
            }
            //MenuButton.METHOD -> {
                //menu.resolver.notifyUpdateMenu(menu.chatId, MethodSelector(menu.chatId, leaf!!, menu.method))
            //}

            MenuButton.RESTART -> {
                val result = wsService.processMessage(leaf.attachedBranch, "RESTART:${leaf.name}")
                menu.nextLevel(result)
            }
        }
    }

}