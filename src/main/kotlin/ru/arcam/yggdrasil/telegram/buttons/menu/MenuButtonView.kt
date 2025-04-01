package ru.arcam.yggdrasil.telegram.buttons.menu

import ru.arcam.yggdrasil.telegram.TelegramSendable
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu

class MenuButtonView(text: String = ""): Button(text) {
    override fun onClick(menu: CarouselMenu) : TelegramSendable? {
        menu.nextLevel(text)
        return null
    }

}