package ru.arcam.yggdrasil.telegram.buttons.rights.role

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.telegram.buttons.rights.group.GroupEditorMenu
import ru.arcam.yggdrasil.telegram.buttons.rights.branch.BranchRightsEditorMenu
import ru.arcam.yggdrasil.telegram.buttons.rights.branch.LeafRightsEditorMenu
import ru.arcam.yggdrasil.users.UserRole

class RoleButtonView(
    private val groupName: String,
    private val role: UserRole,
    private val parentMenu: Menu,
    text: String
) : Button(text, role.name) {
    override fun onClick(menu: Menu) {
        when (parentMenu) {
            is BranchRightsEditorMenu -> parentMenu.setRole(groupName, role)
            is LeafRightsEditorMenu -> parentMenu.setRole(groupName, role)
            is GroupEditorMenu -> parentMenu.setRole(role)
        }
    }
}

