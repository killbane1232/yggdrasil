package ru.arcam.yggdrasil.telegram.buttons.rights

import ru.arcam.yggdrasil.users.UserRight
import ru.arcam.yggdrasil.users.UserRole

internal fun roleFromRight(right: UserRight): UserRole {
    return UserRole.entries.firstOrNull { it.userRight == right } ?: UserRole.NONE
}

