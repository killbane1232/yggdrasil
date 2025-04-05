package ru.arcam.yggdrasil.telegram.buttons.result

import ru.arcam.yggdrasil.telegram.buttons.Menu

class ResultMenu(chatId:Long, text: String): Menu(chatId, listOf(), text) {
    override fun nextLevel(key: String) {

    }

}