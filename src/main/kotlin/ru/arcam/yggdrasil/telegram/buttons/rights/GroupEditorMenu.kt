package ru.arcam.yggdrasil.telegram.buttons.rights

import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.users.GroupResolver

/**
 * Меню для создания групп и редактирования пользователей в группе.
 * Открывается из BranchSelector.
 */
class GroupEditorMenu(chatId: Long) :
    CarouselMenu(chatId, buttons = listOf(), text = "Выберите группу для редактирования") {

    private val groupResolver = GroupResolver.resolver

    override fun getMenu(): KeyboardBuilder {
        val groups = groupResolver.getAllGroups()
        val newButtons = ArrayList<Button>()
        groups.forEach {
            newButtons.add(GroupButtonView(it.groupName, it.userIds.size))
        }
        newButtons.add(AddGroupButtonView())
        buttons = newButtons
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        // Не используем иерархию подменю, все действия через onClick кнопок
    }

    fun editGroupUsers(groupName: String) {
        waitForMessage(
            "Введите логины пользователей для группы $groupName через запятую (полный список, старые значения будут перезаписаны)"
        ) { text ->
            val users = text.split(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toSet()
            groupResolver.updateGroupUsers(groupName, users)
        }
    }

    fun addGroup() {
        waitForMessage(
            "Введите название группы для создания"
        ) { line ->
            groupResolver.addOrUpdateGroupFromLine(line)
        }
    }
}

class GroupButtonView(
    private val groupName: String,
    private val usersCount: Int
) : Button("$groupName ($usersCount)") {
    override fun onClick(menu: Menu) {
        (menu as? GroupEditorMenu)?.editGroupUsers(groupName)
    }
}

class AddGroupButtonView : Button("➕ Добавить группу", "ADD_GROUP") {
    override fun onClick(menu: Menu) {
        (menu as? GroupEditorMenu)?.addGroup()
    }
}


