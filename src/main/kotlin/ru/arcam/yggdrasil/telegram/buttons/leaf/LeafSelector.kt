package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.branch.BranchSelector
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButton
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuSelector

class LeafSelector(chatId: Long, private val leaves: HashMap<String, Leaf> = HashMap()) :
    CarouselMenu(chatId, buttons = leaves.map { LeafButtonView(it.value) }, "Select service on ${leaves.values.first().attachedBranch}") {
    override fun nextLevel(key: String) {
        val leaf = leaves[key]!!
        resolver.notifyUpdateMenu(chatId,
            MenuSelector(chatId,
                leaf,
                MenuButton.entries.filter{
                    it.name != "METHOD" || leaf.hooks.isNotEmpty()
                }.map{ it.button }))
    }
}
