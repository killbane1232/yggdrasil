package ru.arcam.yggdrasil.users

enum class UserRole(val userRight: UserRight) {
    NONE (UserRight(false, false, false)),
    READER (UserRight(true, false, false)),
    ONLY_METHODS (UserRight(true, false, true)),
    ONLY_RESTART (UserRight(true, true, false)),
    ADMIN (UserRight(true, true, true))
}