package ru.arcam.yggdrasil.telegram.buttons.logs

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.result.ResultMenu


class LogsSelector(chatId: Long, val leaf: Leaf, buttons: List<LogsButtonView>): CarouselMenu(chatId, buttons, "Select method for ${leaf.name}") {
    override fun nextLevel(key: String) {
        resolver.notifyUpdateMenu(chatId, ResultMenu(chatId, key))

    }
}
