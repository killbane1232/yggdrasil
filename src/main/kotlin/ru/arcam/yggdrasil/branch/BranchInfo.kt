package ru.arcam.yggdrasil.branch

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import lombok.Builder
import ru.arcam.yggdrasil.leaf.Leaf

@Serializable
@Builder
data class BranchInfo(
    @JsonProperty
    val serviceName : String = "",
    @JsonProperty
    val leaves : List<Leaf> = ArrayList()
)
