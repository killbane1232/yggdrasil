package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButton

class LeafSelector(chatId: Long, var type: MenuButton, leaves: HashMap<String, Leaf> = HashMap(), method: String = "") :
    CarouselMenu(chatId, buttons = leaves.map { LeafButtonView(it.value) }, method) {
    override fun nextLevel(key: String) {
        refresh()
    }
}
