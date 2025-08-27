package ru.arcam.yggdrasil.telegram.buttons.method

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.result.ResultMenu

class MethodSelector(chatId: Long, private val leaf: Leaf) :
    CarouselMenu(chatId, buttons = leaf.hooks.map { MethodButtonView(leaf, it) }, "Select method for ${leaf.name}") {
    override fun nextLevel(key: String) {
        resolver.notifyUpdateMenu(chatId, ResultMenu(chatId, key))
    }
}
