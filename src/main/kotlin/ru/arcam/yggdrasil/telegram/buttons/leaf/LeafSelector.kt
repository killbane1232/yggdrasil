package ru.arcam.yggdrasil.telegram.buttons.leaf

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.UserResolver
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.commands.ICommand
import ru.arcam.yggdrasil.telegram.buttons.rights.branch.BranchRightsEditorMenu
import ru.arcam.yggdrasil.users.GroupResolver

class LeafSelector(chatId: Long, private val leaves: HashMap<String, Leaf> = HashMap(), val command: ICommand) :
    CarouselMenu(chatId, buttons = leaves.map { LeafButtonView(it.value) }, "Select service on ${leaves.values.first().attachedBranch}") {
    override fun nextLevel(key: String) {
        val leaf = leaves[key]!!
        val role = GroupResolver.resolver.getUserRoleByChatId(chatId, leaf.attachedBranch, leaf.name)
        command.takeMenu(chatId, resolver, leaf, role)
    }

    override fun getMenu(): ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder {
        // Добавляем кнопку редактирования прав для Branch в конец меню (если ещё не добавлена)
        if (leaves.isNotEmpty()) {
            val branchName = leaves.values.first().attachedBranch
            if (!buttons.any { it is BranchRightsEntryFromLeafButton }) {
                buttons = buttons.plus(BranchRightsEntryFromLeafButton(branchName))
            }
        }
        return super.getMenu()
    }
}

class BranchRightsEntryFromLeafButton(
    private val branchName: String
) : Button("🛡 Права Branch", "EDIT_BRANCH_RIGHTS") {
    override fun onClick(menu: ru.arcam.yggdrasil.telegram.buttons.Menu) {
        menu.resolver.notifyUpdateMenu(menu.chatId, BranchRightsEditorMenu(menu.chatId, branchName))
    }
}

