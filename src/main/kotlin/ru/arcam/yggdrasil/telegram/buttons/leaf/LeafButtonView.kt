package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.TelegramSendable
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu

class LeafButtonView(private val leaf: Leaf) : Button(leaf.name) {

    override fun onClick(menu: CarouselMenu) : TelegramSendable? {
        menu.nextLevel(leaf.name)

        return null
    }
}