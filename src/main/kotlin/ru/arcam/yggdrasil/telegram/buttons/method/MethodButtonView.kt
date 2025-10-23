package ru.arcam.yggdrasil.telegram.buttons.method

import ru.arcam.yggdrasil.leaf.HookType
import ru.arcam.yggdrasil.leaf.LeafHook
import ru.arcam.yggdrasil.telegram.buttons.Button
import ru.arcam.yggdrasil.telegram.buttons.Menu
import ru.arcam.yggdrasil.leaf.Leaf
import ru.arcam.yggdrasil.utils.AuditLogger
import ru.arcam.yggdrasil.ws.WebSocketService

class MethodButtonView(private val leaf: Leaf, private val hook: LeafHook) : Button(hook.name) {
    var wsService = WebSocketService.wsService

    override fun onClick(menu: Menu) {
        if (hook.hookFields.isNotEmpty()) {
            val listOfParams = hook.hookFields.toList()
            getParams(menu, arrayListOf(), listOfParams, 0)
            return
        }

        val message = "METHOD:${leaf.name}:${hook.name}"
        AuditLogger.logWsCall(menu.chatId, message)
        val result = wsService.processMessage(leaf.attachedBranch, message)
        menu.nextLevel(result)
    }

    private fun getParams(menu: Menu, resultsList: ArrayList<String>, listOfParams: List<Pair<String, HookType>>, idx: Int) {
        menu.waitForMessage(listOfParams[idx].first) { num ->
            val clearNum = num.trim()
            if (clearNum.isBlank()) {
                menu.resolver.bot?.sendKeyBoard(menu.chatId)
                return@waitForMessage
            }
            resultsList.add(listOfParams[idx].first + ";" + clearNum)
            var newIdx = idx + 1
            if (newIdx < listOfParams.size)
                getParams(menu, resultsList, listOfParams, newIdx)
            else {
                val message = "METHOD:${leaf.name}:${hook.name}:${resultsList.joinToString(":")}"
                AuditLogger.logWsCall(menu.chatId, message)
                wsService.processMessageAsync(leaf.attachedBranch, message) { result ->
                    menu.nextLevel(result)
                    menu.resolver.bot?.sendKeyBoard(menu.chatId)
                }
            }
        }
    }
} 