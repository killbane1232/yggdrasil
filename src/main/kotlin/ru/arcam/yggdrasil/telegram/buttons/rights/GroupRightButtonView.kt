package ru.arcam.yggdrasil.telegram.buttons.rights

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.users.UserRole

class GroupRightButtonView(
    private val groupName: String,
    private val role: UserRole
) : Button("$groupName: ${role.name}", groupName) {
    override fun onClick(menu: Menu) {
        when (menu) {
            is BranchRightsEditorMenu -> menu.nextLevel(groupName)
            is LeafRightsEditorMenu -> menu.nextLevel(groupName)
        }
    }
}

