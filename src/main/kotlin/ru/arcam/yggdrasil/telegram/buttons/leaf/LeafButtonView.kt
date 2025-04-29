package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu

class LeafButtonView(private val leaf: Leaf) : Button(
    when (leaf.status) {
        "RUNNING" -> "✅ "
        "STOPPED" -> "\uD83D\uDEAB "
        else -> ""
    } + leaf.name, leaf.name) {

    override fun onClick(menu: Menu) {
        menu.nextLevel(leaf.name)
    }
}