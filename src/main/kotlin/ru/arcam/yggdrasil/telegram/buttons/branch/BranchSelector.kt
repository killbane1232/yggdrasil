package ru.arcam.yggdrasil.telegram.buttons.branch

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.branch.BranchInfo
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.leaf.LeafButtonView
import ru.arcam.yggdrasil.telegram.buttons.leaf.LeafSelector

class BranchSelector(chatId: Long): CarouselMenu(chatId, ArrayList()) {
    var branches = HashMap<String, BranchInfo>()
    var leaves = HashMap<String, Leaf>()

    override fun getMenu(): InlineKeyboardMarkup {
        buttons = ArrayList()
        branches = BranchController.branchStorage.storage
        for(i in branches.keys)
            buttons = buttons.plus(BranchButtonView(i, branches[i]!!))
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        buttons = ArrayList()
        refresh()
        for (leaf in branches[key]!!.leaves) {
            leaves[leaf.name] = leaf
            buttons.plus(LeafButtonView(leaf))
        }
        resolver.notifyUpdateMenu(chatId, LeafSelector(chatId, leaves))
    }
}
