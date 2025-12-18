package ru.arcam.yggdrasil.telegram.buttons.rights.branch

import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu

class SaveLeafRightsButtonView : Button("💾 Сохранить права (Leaf)", "SAVE_LEAF_RIGHTS") {
    override fun onClick(menu: Menu) {
        (menu as? LeafRightsEditorMenu)?.saveRights()
    }
}

