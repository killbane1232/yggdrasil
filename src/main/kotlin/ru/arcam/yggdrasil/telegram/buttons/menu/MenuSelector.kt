package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector


class MenuSelector(chatId: Long, buttons: List<MenuButtonView>): CarouselMenu(chatId, buttons, method = "MENU") {
    override fun nextLevel(key: String) {
        resolver.notifyUpdateMenu(chatId, BranchSelector(chatId, MenuButton.valueOf(key.uppercase())))
    }
}
