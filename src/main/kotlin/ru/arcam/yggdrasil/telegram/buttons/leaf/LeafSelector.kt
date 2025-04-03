package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButton
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuSelector

class LeafSelector(chatId: Long, val leaves: HashMap<String, Leaf> = HashMap(), method: String = "") :
    CarouselMenu(chatId, buttons = leaves.map { LeafButtonView(it.value) }, method) {
    override fun nextLevel(key: String) {
        val leaf = leaves.values.first{x -> x.name == key}
        resolver.notifyUpdateMenu(chatId, MenuSelector(chatId, leaf, MenuButton.entries.map{ it.button }))
    }
}
