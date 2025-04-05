package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu

class LeafButtonView(private val leaf: Leaf) : Button(leaf.name) {

    override fun onClick(menu: Menu) {
        menu.nextLevel(leaf.name)
    }
}