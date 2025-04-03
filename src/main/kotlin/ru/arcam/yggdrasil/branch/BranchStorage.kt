package ru.arcam.yggdrasil.branch

import java.time.LocalDateTime

class BranchStorage {
    var storage: HashMap<String, BranchInfo> = HashMap()
    var storageCleaner: HashMap<String, LocalDateTime> = HashMap()
    private val timeoutMinutes = 10L

    fun cleanup() {
        val now = LocalDateTime.now()
        storageCleaner.forEach { (name, timestamp) ->
            if (now.minusSeconds(timeoutMinutes).isAfter(timestamp)) {
                storage.remove(name)
                storageCleaner.remove(name)
            }
        }
    }
}