package ru.arcam.yggdrasil.branch

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class BranchController {
    @MessageMapping("/assing")
    fun processMessage(@Payload branchInfo: BranchInfo?) {
        branchStorage.storage[branchInfo!!.serviceName] = branchInfo
    }

    companion object {
        val branchStorage = BranchStorage()
    }
}