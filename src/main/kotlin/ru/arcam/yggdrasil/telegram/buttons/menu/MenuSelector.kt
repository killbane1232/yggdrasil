package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.telegram.buttons.result.ResultMenu
import ru.arcam.yggdrasil.telegram.buttons.rights.branch.LeafRightsEditorMenu
import ru.arcam.yggdrasil.users.GroupResolver


class MenuSelector(chatId: Long, val leaf: Leaf, buttons: List<MenuButtonView>) :
    CarouselMenu(chatId, buttons, "Select method for ${leaf.name}") {

    override fun getMenu(): ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder {
        // Добавляем кнопку редактирования прав для Leaf в конец меню (если ещё не добавлена)
        if (!buttons.any { it is LeafRightsEntryFromMenuButton }) {
            val role = GroupResolver.resolver.getUserRoleByChatId(chatId, leaf.attachedBranch, leaf.name)
            if (role.admin) {
                buttons = buttons.plus(LeafRightsEntryFromMenuButton())
            }
        }
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        resolver.notifyUpdateMenu(chatId, ResultMenu(chatId, key))
    }
}

class LeafRightsEntryFromMenuButton :
    Button("🛡 Права Leaf", "EDIT_LEAF_RIGHTS") {
    override fun onClick(menu: Menu) {
        val m = menu as MenuSelector
        val leaf = m.leaf
        menu.resolver.notifyUpdateMenu(
            menu.chatId,
            LeafRightsEditorMenu(menu.chatId, leaf.attachedBranch, leaf.name)
        )
    }
}

