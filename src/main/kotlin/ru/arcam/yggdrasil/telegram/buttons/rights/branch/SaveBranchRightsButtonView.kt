package ru.arcam.yggdrasil.telegram.buttons.rights.branch

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu

class SaveBranchRightsButtonView : Button("💾 Сохранить права (Branch)", "SAVE_BRANCH_RIGHTS") {
    override fun onClick(menu: Menu) {
        (menu as? BranchRightsEditorMenu)?.saveRights()
    }
}

