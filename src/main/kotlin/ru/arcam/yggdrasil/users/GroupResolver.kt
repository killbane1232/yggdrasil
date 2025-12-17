package ru.arcam.yggdrasil.telegram

import org.springframework.context.annotation.Configuration
import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.users.Group
import ru.arcam.yggdrasil.users.UserRight
import ru.arcam.yggdrasil.users.UserRole
import ru.arcam.yggdrasil.utils.ConfigReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.collections.HashMap


@Configuration
class GroupResolver {
    // Список всех групп из group.config
    private val groupsByName: MutableList<Group> = ArrayList()
    var lastCheck = LocalDateTime.now()
    var chatIdToUser: HashMap<Long, String> = HashMap()

    init {
        readConfig()
    }

    private fun readConfig() {
        val config = ConfigReader.loadConfig("group.config")
        if (config != null && config.exists()) {
            groupsByName.clear()
            val lines: List<String> = Files.readAllLines(Paths.get(config.path), StandardCharsets.UTF_8)
            for (line in lines) {
                val lineSplit = line.split(':')
                if (lineSplit.size > 1
                    && UserRole.entries.any { it.name == lineSplit[1] }) {
                    val userSplit = lineSplit[2].split(',')
                    if (userSplit.size > 0) {
                        val newGroup = Group(
                            groupName = lineSplit[0],
                            globalRole = UserRole.valueOf(lineSplit[1]),
                            userIds = userSplit.filter { it.isNotBlank() }.toSet()
                        )
                        groupsByName.add(newGroup)
                    }
                }
            }
            lastCheck = LocalDateTime.now()
        }
    }

    @Synchronized
    fun getAllGroups(): List<Group> {
        // Возвращаем копии, чтобы не давать наружу изменять внутренний список напрямую
        return groupsByName.map {
            Group(
                groupName = it.groupName,
                globalRole = it.globalRole,
                serverAllowedServers = it.serverAllowedServers,
                serverAllowedServices = it.serverAllowedServices,
                userIds = HashSet(it.userIds)
            )
        }
    }

    @Synchronized
    private fun saveGroups(newGroups: List<Group>) {
        val config = ConfigReader.loadConfig("group.config") ?: return
        val path = Paths.get(config.path)
        val lines = newGroups.map { group ->
            val users = group.userIds.joinToString(",")
            "${group.groupName}:${group.globalRole.name}:$users"
        }
        Files.write(path, lines, StandardCharsets.UTF_8)
        groupsByName.clear()
        groupsByName.addAll(newGroups)
        lastCheck = LocalDateTime.now()
    }

    @Synchronized
    fun updateGroupUsers(groupName: String, newUserIds: Set<String>) {
        val current = getAllGroups().toMutableList()
        val idx = current.indexOfFirst { it.groupName == groupName }
        if (idx >= 0) {
            val g = current[idx]
            current[idx] = g.copy(userIds = newUserIds)
            saveGroups(current)
        }
    }

    @Synchronized
    fun addOrUpdateGroupFromLine(name: String) {
        val current = getAllGroups().toMutableList()
        val idx = current.indexOfFirst { it.groupName == name }
        val newGroup = Group(
            groupName = name,
            globalRole = UserRole.NONE,
            serverAllowedServers = if (idx >= 0) current[idx].serverAllowedServers else emptyList(),
            serverAllowedServices = if (idx >= 0) current[idx].serverAllowedServices else emptyMap(),
            userIds = if (idx >= 0) current[idx].userIds else emptySet()
        )
        if (idx >= 0) {
            current[idx] = newGroup
        } else {
            current.add(newGroup)
        }
        saveGroups(current)
    }

    /**
     * Агрегация прав: если у любой из групп есть право R/W/X – у пользователя тоже есть это право.
     */
    private fun aggregateRights(rights: Collection<UserRight>): UserRight {
        if (rights.isEmpty()) {
            return UserRole.NONE.userRight
        }
        var read = false
        var write = false
        var execute = false
        rights.forEach {
            if (it.read) read = true
            if (it.write) write = true
            if (it.execute) execute = true
        }
        return UserRight(read, write, execute)
    }

    @Synchronized
    fun getGroupRoleByName(name: String, branchName: String? = null, leafName: String? = null): UserRight {
        val now = LocalDateTime.now()
        if (now.minusSeconds(15) > lastCheck) {
            readConfig()
        }
        if (groupsByName.isEmpty()) {
            return UserRole.ADMIN.userRight
        }
        val userGroups = groupsByName.filter { it.userIds.contains(name) }
        if (userGroups.isEmpty()) {
            return UserRole.NONE.userRight
        }

        // Глобальные права по всем группам пользователя
        val role = aggregateRights(userGroups.map { it.globalRole.userRight })

        if (branchName == null)
            return role

        val storage = BranchController.branchStorage.storage
        if (!storage.containsKey(branchName))
            return role
        val branch = storage[branchName]!!

        // Права на уровне сервиса для всех групп пользователя:
        // ключи вида "G@ИмяГруппы" в BranchInfo.allowedUsers
        var servRole = role
        if (branch.allowedUsers.isNotEmpty()) {
            val groupBranchRights = userGroups.mapNotNull { group ->
                branch.allowedUsers["G@${group.groupName}"]
            }
            if (groupBranchRights.isNotEmpty()) {
                servRole = aggregateRights(groupBranchRights + role)
            }
        }

        val leaf = branch.leaves.firstOrNull { it.name == leafName }
        if (leaf == null)
            return UserRight(
                role.read || servRole.read,
                role.write || servRole.write,
                role.execute || servRole.execute
            )

        // Права на уровне лифа для всех групп пользователя:
        // ключи вида "G@ИмяГруппы" в Leaf.allowedUsers
        var leafRole = servRole
        if (leaf.allowedUsers.isNotEmpty()) {
            val groupLeafRights = userGroups.mapNotNull { group ->
                leaf.allowedUsers["G@${group.groupName}"]
            }
            if (groupLeafRights.isNotEmpty()) {
                leafRole = aggregateRights(groupLeafRights + servRole)
            }
        }

        return UserRight(
            role.read || servRole.read && leafRole.read,
            role.write || servRole.write && leafRole.write,
            role.execute || servRole.execute && leafRole.execute
        )
    }

    @Synchronized
    fun getUserRoleByChatId(chatId: Long, branchName: String? = null, leafName: String? = null): UserRight {
        return getUserRoleByName(chatIdToUser[chatId]!!, branchName, leafName)
    }

    companion object {
        var resolver: GroupResolver = GroupResolver()
    }
}