package ru.arcam.yggdrasil.telegram.buttons.menu

enum class MenuButton(val button: MenuButtonView) {
    METHOD (MenuButtonView("Method")),
    STATUS (MenuButtonView("Status")),
    STOP (MenuButtonView("Stop")),
    START (MenuButtonView("Start")),
    RESTART (MenuButtonView("Restart"))
}