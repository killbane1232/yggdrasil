package ru.arcam.yggdrasil.telegram

interface ITelegramMessageMappable {
    fun mapToText()
    fun mapButtons()
}