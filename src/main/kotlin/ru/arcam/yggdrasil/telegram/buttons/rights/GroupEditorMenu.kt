package ru.arcam.yggdrasil.telegram.buttons.rights

import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.users.GroupResolver
import ru.arcam.yggdrasil.users.UserRole

/**
 * Меню для создания групп и редактирования пользователей в группе.
 * Открывается из BranchSelector.
 */
class GroupEditorMenu(chatId: Long, val groupName: String) :
    CarouselMenu(chatId, buttons = listOf(), text = "Выберите группу для редактирования") {

    private val groupResolver = GroupResolver.resolver
    var userRole: UserRole = UserRole.NONE
    private var users: Set<String> = emptySet()


    override fun getMenu(): KeyboardBuilder {
        val groups = groupResolver.getAllGroups()
        val newButtons = ArrayList<Button>()
        val group = groups.first{it.groupName == groupName}
        userRole = group.globalRole
        users = group.userNames
        newButtons.add(GroupEditButtonView(groupName, users.size))
        newButtons.add(GroupChangeRightsButtonView(groupName, userRole))
        newButtons.add(GroupSaveButtonView())
        buttons = newButtons
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        // Не используем иерархию подменю, все действия через onClick кнопок
    }

    fun editGroupUsers(groupName: String) {
        waitForMessage(
            "Введите логины пользователей для группы $groupName через запятую (полный список, старые значения будут перезаписаны)\n" +
            "Текущий список:\n" +
            "```\n${groupResolver.getUsersByGroup(groupName).joinToString(",")}\n```"
        ) { text ->
            val users = text.split(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toSet()
            groupResolver.updateGroupUsers(groupName, users)
            resolver.lastMenuChanged[chatId] = true
            resolver.bot?.sendKeyBoard(chatId)
        }
    }

    fun editGroupRights(groupName: String) {
        resolver.notifyUpdateMenu(chatId, RoleSelectorMenu(chatId, groupName, this))
    }

    fun setRole(role: UserRole) {
        userRole = role
        resolver.lastMenuChanged[chatId] = true
        resolver.goBack(chatId)
    }

    fun saveGroup() {
        groupResolver.updateGroup(groupName, userRole, users)
    }
}

class GroupEditButtonView(
    private val groupName: String,
    usersCount: Int
) : Button("$usersCount users") {
    override fun onClick(menu: Menu) {
        (menu as? GroupEditorMenu)?.editGroupUsers(groupName)
    }
}

class GroupSaveButtonView: Button("Save") {
    override fun onClick(menu: Menu) {
        (menu as? GroupEditorMenu)?.saveGroup()
    }
}

class GroupChangeRightsButtonView(
    private val groupName: String,
    userRole: UserRole
) : Button(userRole.name) {
    override fun onClick(menu: Menu) {
        (menu as? GroupEditorMenu)?.editGroupRights(groupName)
    }
}


