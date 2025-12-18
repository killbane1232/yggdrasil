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
class GroupSelectorMenu(chatId: Long) :
    CarouselMenu(chatId, buttons = listOf(), text = "Выберите группу для редактирования") {

    private val groupResolver = GroupResolver.resolver

    override fun getMenu(): KeyboardBuilder {
        val groups = groupResolver.getAllGroups()
        val newButtons = ArrayList<Button>()
        groups.forEach {
            newButtons.add(GroupButtonView(it.groupName))
        }
        newButtons.add(AddGroupButtonView())
        buttons = newButtons
        return super.getMenu()
    }

    override fun nextLevel(key: String) {
        // Не используем иерархию подменю, все действия через onClick кнопок
    }

    fun editGroupUsers(groupName: String) {
        resolver.notifyUpdateMenu(chatId, GroupEditorMenu(chatId, groupName))
    }

    fun addGroup() {
        waitForMessage(
            "Введите название группы для создания"
        ) { line ->
            groupResolver.addGroup(line)
            resolver.lastMenuChanged[chatId] = true
            resolver.bot?.sendKeyBoard(chatId)
        }
    }
}

class GroupButtonView(
    private val groupName: String
) : Button(groupName) {
    override fun onClick(menu: Menu) {
        (menu as? GroupSelectorMenu)?.editGroupUsers(groupName)
    }
}

class AddGroupButtonView : Button("➕ Добавить группу", "ADD_GROUP") {
    override fun onClick(menu: Menu) {
        (menu as? GroupSelectorMenu)?.addGroup()
    }
}


