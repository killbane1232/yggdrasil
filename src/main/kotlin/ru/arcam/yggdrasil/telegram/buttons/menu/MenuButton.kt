package ru.arcam.yggdrasil.telegram.buttons.menu

enum class MenuButton(val button: MenuButtonView) {
    // METHOD (MenuButtonView("\uD83D\uDD22 Method", "Method")),
    STATUS (MenuButtonView("❔ Status", "Status")),
    STOP (MenuButtonView("⏹\uFE0F Stop", "Stop")),
    START (MenuButtonView("▶\uFE0F Start", "Start")),
    RESTART (MenuButtonView("\uD83D\uDD04 Restart", "Restart"))
}