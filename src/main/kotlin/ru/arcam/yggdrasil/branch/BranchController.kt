package ru.arcam.yggdrasil.branch

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import java.time.LocalDateTime

@Controller
class BranchController {
    @MessageMapping("/assing")
    fun processMessage(@Payload branchInfo: BranchInfo?) {
        branchStorage.storage[branchInfo!!.serviceName] = branchInfo
        branchStorage.storageCleaner[branchInfo!!.serviceName] = LocalDateTime.now()
    }

    @Scheduled(fixedRate = 10000)
    fun cleanStorage() {
        branchStorage.cleanup()
    }

    companion object {
        val branchStorage = BranchStorage()
    }
}