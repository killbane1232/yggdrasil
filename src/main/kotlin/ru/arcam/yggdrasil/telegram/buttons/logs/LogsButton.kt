package ru.arcam.yggdrasil.telegram.buttons.logs

enum class LogsButton(val button: LogsButtonView) {
    TAIL (LogsButtonView("Tail", "TAIL")),
    TAIL_N (LogsButtonView("Tail N", "TAIL_N")),
}