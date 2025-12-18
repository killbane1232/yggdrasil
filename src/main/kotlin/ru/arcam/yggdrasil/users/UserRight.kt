package ru.arcam.yggdrasil.users

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import lombok.Builder
import ru.arcam.yggdrasil.telegram.buttons.menu.MenuButton
import java.util.ArrayList

@Serializable
@Builder
data class UserRight(
    @JsonProperty
    val read: Boolean,
    @JsonProperty
    val write: Boolean,
    @JsonProperty
    val execute: Boolean,
    @JsonIgnore
    val admin: Boolean = false
) {
    fun getMethods(): List<MenuButton> {
        val methods = ArrayList<MenuButton>()
        if (read) {
            methods.add(MenuButton.STATUS)
        }
        if (write) {
            if (!read) {
                methods.add(MenuButton.STATUS)
            }
            methods.addAll(listOf(MenuButton.START, MenuButton.STOP, MenuButton.RESTART))
        }
        if (execute) {
            if (!read) {
                methods.add(MenuButton.STATUS)
            }
            methods.add(MenuButton.METHOD)
        }
        return methods
    }

    fun isAny() : Boolean {
        return read || write || execute
    }
}