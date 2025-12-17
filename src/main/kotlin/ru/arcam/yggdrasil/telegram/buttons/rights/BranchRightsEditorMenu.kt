package ru.arcam.yggdrasil.telegram.buttons.rights

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu
import ru.arcam.yggdrasil.telegram.buttons.KeyboardBuilder
import ru.arcam.yggdrasil.users.UserRight
import ru.arcam.yggdrasil.users.UserRole
import ru.arcam.yggdrasil.utils.AuditLogger
import ru.arcam.yggdrasil.ws.WebSocketService
import java.util.ArrayList

/**
 * Редактирование прав для Branch (RIGHTS:NULL:...).
 * Открывается из LeafSelector.
 */
class BranchRightsEditorMenu(
    chatId: Long,
    private val branchName: String
) : CarouselMenu(chatId, buttons = listOf(), text = "Права групп для $branchName") {

    private val wsService = WebSocketService.wsService
    internal val groupRoles: MutableMap<String, UserRole> = LinkedHashMap()

    override fun getMenu(): KeyboardBuilder {
        if (groupRoles.isEmpty()) {
            initFromBranch()
        }
        val newButtons = ArrayList<Button>()
        groupRoles.forEach { (group, role) ->
            newButtons.add(GroupRightButtonView(group, role))
        }
        newButtons.add(SaveBranchRightsButtonView())
        buttons = newButtons
        return super.getMenu()
    }

    private fun initFromBranch() {
        val storage = BranchController.branchStorage.storage
        val branch = storage[branchName] ?: return
        // Берём все существующие ключи G@*
        branch.allowedUsers.forEach { (key, right) ->
            if (key.startsWith("G@")) {
                val groupName = key.removePrefix("G@")
                groupRoles[groupName] = roleFromRight(right)
            }
        }
    }

    override fun nextLevel(key: String) {
        // Открываем меню выбора роли для группы
        resolver.notifyUpdateMenu(chatId, RoleSelectorMenu(chatId, key, this))
    }

    fun setRole(groupName: String, role: UserRole) {
        groupRoles[groupName] = role
        resolver.lastMenuChanged[chatId] = true
        resolver.notifyUpdateMenu(chatId, this)
    }

    fun saveRights() {
        val rights: MutableMap<String, UserRight> = LinkedHashMap()
        groupRoles.forEach { (group, role) ->
            rights["G@$group"] = role.userRight
        }
        val json = Json.encodeToString(rights)
        val message = "RIGHTS:NULL:$json"
        AuditLogger.logWsCall(chatId, message)
        wsService.processMessage(branchName, message)
    }
}

