package ru.arcam.yggdrasil.telegram.buttons.rights

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.users.UserRole
import java.util.ArrayList

/**
 * Меню выбора роли для группы.
 * Открывается при клике на группу в BranchRightsEditorMenu или LeafRightsEditorMenu.
 */
class RoleSelectorMenu(
    chatId: Long,
    private val groupName: String,
    private val parentMenu: Menu
) : Menu(chatId, buttons = ArrayList(), text = "Выберите роль для группы: $groupName") {

    override fun getMenu(): KeyboardBuilder {
        val roleButtons = ArrayList<Button>()
        UserRole.entries.forEach { role ->
            val isCurrent = when (parentMenu) {
                is BranchRightsEditorMenu -> parentMenu.groupRoles[groupName] == role
                is LeafRightsEditorMenu -> parentMenu.groupRoles[groupName] == role
                else -> false
            }
            val prefix = if (isCurrent) "✓ " else ""
            roleButtons.add(RoleButtonView(groupName, role, parentMenu, "$prefix${role.name}"))
        }
        buttons = roleButtons
        return KeyboardBuilder(text, buttons)
    }

    override fun nextLevel(key: String) {
        // Не используется, все через onClick
    }
}

