package ru.arcam.yggdrasil.telegram.buttons.branch

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu

class BranchButtonView(private val serviceName: String): Button(serviceName) {
    override fun onClick(menu: Menu) {
        menu.nextLevel(serviceName)
    }
}