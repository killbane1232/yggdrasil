package ru.arcam.yggdrasil.telegram.buttons

abstract class CarouselMenu (chatId: Long, buttons: List<Button>, text: String) : Menu(chatId, buttons, text) {
    val MAX_SIZE = 5
    var idx = 1
    var maxPages = 1
    private val NEXT = "NEXT"
    private val PREVIOUS = "PREVIOUS"

    override fun getMenu(): KeyboardBuilder {
        maxPages = buttons.size / MAX_SIZE + if (buttons.size % MAX_SIZE > 0) 1 else 0
        if (maxPages == 0)
            maxPages = 1
        val buttonIdx = (idx - 1) * MAX_SIZE
        var i = 0
        var footer: String? = "${idx}/${maxPages}"
        if (maxPages == 1)
            footer = null
        val builder = KeyboardBuilder(text, arrayListOf(), footer)
        while (i < MAX_SIZE && i + buttonIdx < buttons.size) {
            builder.buttons.add(buttons[i + buttonIdx])
            i++
        }
        return builder
    }

    override fun onClick(callbackData: String) {
        when (callbackData) {
            NEXT -> {
                val now = idx
                idx = (idx) % maxPages + 1
                resolver.lastMenuChanged[chatId] = idx != now
            }
            PREVIOUS -> {
                val now = idx
                idx = (idx - 2 + maxPages) % maxPages + 1
                resolver.lastMenuChanged[chatId] = idx != now
            }
            else -> super.onClick(callbackData)
        }
    }

    fun refresh() {
        idx = 1
    }
}