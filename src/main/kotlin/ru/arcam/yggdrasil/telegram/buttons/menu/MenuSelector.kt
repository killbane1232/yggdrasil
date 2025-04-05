package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.result.ResultMenu


class MenuSelector(chatId: Long, val leaf: Leaf, buttons: List<MenuButtonView>): CarouselMenu(chatId, buttons, "Select method for ${leaf.name}") {
    override fun nextLevel(key: String) {
        resolver.notifyUpdateMenu(chatId, ResultMenu(chatId, key))
    }
}
