package ru.arcam.yggdrasil.telegram

import org.springframework.context.annotation.Configuration
import ru.arcam.yggdrasil.branch.BranchController
import ru.arcam.yggdrasil.users.User
import ru.arcam.yggdrasil.users.UserRight
import ru.arcam.yggdrasil.users.UserRole
import ru.arcam.yggdrasil.utils.ConfigReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.HashMap


@Configuration
class UserResolver {
    final val userByName: HashMap<String, User> = HashMap()
    init {
        val config = ConfigReader.loadConfig("user.config")
        if (config != null && config.exists()) {
            val lines: List<String> = Files.readAllLines(Paths.get(config.path), StandardCharsets.UTF_8)
            for (line in lines) {
                val lineSplit = line.split(':')
                if (lineSplit.size > 1 && UserRole.entries.any { it.name == lineSplit[1] }) {
                    userByName[lineSplit[0]] = User(lineSplit[0], UserRole.valueOf(lineSplit[1]))
                }
            }
        }
    }

    @Synchronized
    fun getUserRoleByName(name: String, branchName: String? = null, leafName: String? = null): UserRight {
        if (userByName.isEmpty()) {
            return UserRole.ADMIN.userRight
        }
        if (!userByName.containsKey(name))
            return UserRole.NONE.userRight
        val user = userByName[name]!!
        var role = user.globalRole.userRight
        if (branchName == null)
            return role
        val storage = BranchController.branchStorage.storage
        if (!storage.containsKey(branchName))
            return role
        val branch = storage[branchName]!!
        var servRole = role
        if (branch.allowedUsers.isNotEmpty() && branch.allowedUsers.containsKey(name)) {
            servRole = branch.allowedUsers[branchName]!!
        }
        val leaf = branch.leaves.firstOrNull { it.name == leafName }
        if (leaf == null)
            return UserRight(
                role.read || servRole.read,
                role.write || servRole.write,
                role.execute || servRole.execute
            )
        var leafRole = role
        if (leaf.allowedUsers.isNotEmpty() && leaf.allowedUsers.containsKey(name)) {
            leafRole = leaf.allowedUsers[name]!!
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
        var resolver: UserResolver = UserResolver()
        var chatIdToUser: HashMap<Long, String> = HashMap()
    }
}