package ru.arcam.yggdrasil.telegram.buttons.branch

import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.branch.BranchInfo
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.UserResolver
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.leaf.LeafSelector
import ru.arcam.yggdrasil.telegram.commands.ICommand
import ru.arcam.yggdrasil.telegram.buttons.rights.group.GroupSelectorMenu
import ru.arcam.yggdrasil.users.GroupResolver
import ru.arcam.yggdrasil.users.UserRole

class BranchSelector(chatId: Long, val command: ICommand): CarouselMenu(chatId, ArrayList(), "Select server") {
    var branches = HashMap<String, BranchInfo>()

    override fun getMenu(): KeyboardBuilder {
        buttons = ArrayList()
        branches = BranchController.branchStorage.storage
        for(i in branches.keys) {
            val role = UserResolver.resolver.getUserRoleByChatId(chatId, i)
            if (role.isAny())
                buttons = buttons.plus(BranchButtonView(i))
        }
        // Кнопка управления группами/пользователями в конце меню (только для ADMIN)
        val globalRole = GroupResolver.resolver.getUserRoleEnumByChatId(chatId)
        val globalRole2 = UserResolver.resolver.getUserRoleEnumByChatId(chatId)
        if (globalRole == UserRole.ADMIN || globalRole2 == UserRole.ADMIN) {
            buttons = buttons.plus(BranchGroupsEditButton())
        }
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        val leaves = HashMap<String, Leaf>()
        for (leaf in branches[key]!!.leaves) {
            val role = UserResolver.resolver.getUserRoleByChatId(chatId, key, leaf.name)
            if (role.isAny())
                leaves[leaf.name] = leaf
        }
        resolver.notifyUpdateMenu(chatId, LeafSelector(chatId, leaves, command))
    }
}

class BranchGroupsEditButton : Button("⚙ Группы и пользователи", "EDIT_GROUPS") {
    override fun onClick(menu: ru.arcam.yggdrasil.telegram.buttons.Menu) {
        menu.resolver.notifyUpdateMenu(menu.chatId, GroupSelectorMenu(menu.chatId))
    }
}

