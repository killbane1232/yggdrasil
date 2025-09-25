package ru.arcam.yggdrasil.telegram.commands

import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.telegram.StateResolver
import ru.arcam.yggdrasil.telegram.TelegramBot
import ru.arcam.yggdrasil.users.UserRight

sealed class ICommand {
    abstract fun runCommand(bot: TelegramBot, chatId: Long);
    abstract fun takeMenu(chatId: Long, resolver: StateResolver, leaf: Leaf, role: UserRight)
}