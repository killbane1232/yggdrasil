package ru.arcam.yggdrasil.telegram.buttons.branch

import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.branch.BranchInfo
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.leaf.LeafSelector

class BranchSelector(chatId: Long): CarouselMenu(chatId, ArrayList(), "Select server") {
    var branches = HashMap<String, BranchInfo>()

    override fun getMenu(): KeyboardBuilder {
        buttons = ArrayList()
        branches = BranchController.branchStorage.storage
        for(i in branches.keys)
            buttons = buttons.plus(BranchButtonView(i))
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        val leaves = HashMap<String, Leaf>()
        for (leaf in branches[key]!!.leaves) {
            leaves[leaf.name] = leaf
        }
        resolver.notifyUpdateMenu(chatId, LeafSelector(chatId, leaves))
    }
}
