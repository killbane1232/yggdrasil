package ru.arcam.yggdrasil.users

import java.util.ArrayList

data class Group (
    val groupName: String,
    val globalRole: UserRole = UserRole.NONE,
    val serverAllowedServers: List<String> = ArrayList(),
    val serverAllowedServices: Map<String, List<String>> = HashMap(),
    var userIds: Set<String> = HashSet()
)