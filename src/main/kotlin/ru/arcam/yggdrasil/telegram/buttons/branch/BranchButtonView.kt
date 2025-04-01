package ru.arcam.yggdrasil.telegram.buttons.branch

import ru.arcam.yggdrasil.branch.BranchInfo
import ru.arcam.yggdrasil.telegram.TelegramSendable
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.CarouselMenu

class BranchButtonView(private val serviceName: String, private val branchInfo: BranchInfo): Button(serviceName) {
    override fun onClick(menu: CarouselMenu) : TelegramSendable? {
        menu.nextLevel(serviceName)
        return null
    }
}