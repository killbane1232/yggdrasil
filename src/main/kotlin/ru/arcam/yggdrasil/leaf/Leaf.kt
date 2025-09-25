package ru.arcam.yggdrasil.leaf

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import ru.arcam.yggdrasil.users.UserRight

@Serializable
data class Leaf(
    @JsonProperty
    val name: String = "",
    @JsonProperty
    val status: String = "",
    @JsonProperty
    val attachedBranch: String = "",
    @JsonProperty
    val allowedUsers : Map<String, UserRight> = hashMapOf(),
    @JsonProperty
    val allowHierarchy: Boolean = true,
    @JsonProperty
    val hooks: List<LeafHook> = ArrayList()
)
