package ru.arcam.yggdrasil.telegram.buttons.method

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu

class MethodSelector(chatId: Long, private val leaf: Leaf, method: String = "") :
    CarouselMenu(chatId, buttons = leaf.hooks.map { MethodButtonView(leaf, it) }) {
    override fun nextLevel(key: String) {
        refresh()
    }
}
