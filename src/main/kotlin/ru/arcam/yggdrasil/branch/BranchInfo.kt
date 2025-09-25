package ru.arcam.yggdrasil.branch

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import lombok.Builder
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.users.UserRight

@Serializable
@Builder
data class BranchInfo(
    @JsonProperty
    val serviceName : String = "",
    @JsonProperty
    val allowedUsers : Map<String, UserRight> = hashMapOf(),
    @JsonProperty
    val allowHierarchy: Boolean = true,
    @JsonProperty
    val leaves : List<Leaf> = ArrayList()
)
