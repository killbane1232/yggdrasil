package ru.arcam.yggdrasil.leaf

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class Leaf(
    @JsonProperty
    val name: String = "",
    @JsonProperty
    val status: String = "",
    @JsonProperty
    val attachedBranch: String = "",
    @JsonProperty
    val hooks: List<LeafHook> = ArrayList()
)
