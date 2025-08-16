package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.commands.ICommand

class LeafSelector(chatId: Long, private val leaves: HashMap<String, Leaf> = HashMap(), val command: ICommand) :
    CarouselMenu(chatId, buttons = leaves.map { LeafButtonView(it.value) }, "Select service on ${leaves.values.first().attachedBranch}") {
    override fun nextLevel(key: String) {
        val leaf = leaves[key]!!
        command.takeMenu(chatId, resolver, leaf)
    }
}
