package ru.arcam.yggdrasil.telegram.commands

import org.springframework.stereotype.Component
import ru.arcam.yggdrasil.telegram.TelegramBot

@Component(value = "/menu")
class MenuCommand(): ICommand() {
    override fun runCommand(bot: TelegramBot) {

    }

}