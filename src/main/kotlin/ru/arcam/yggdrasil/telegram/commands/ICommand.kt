package ru.arcam.yggdrasil.telegram.commands

import ru.arcam.yggdrasil.telegram.TelegramBot

sealed class ICommand {
    abstract fun runCommand(bot: TelegramBot);
}