package ru.arcam.yggdrasil.utils

import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AuditLogger {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val logger = LoggerFactory.getLogger("AUDIT")
    private val auditFile = File("log/audit.log")
    
    fun logWsCall(chatId: Long, message: String) {
        val timestamp = LocalDateTime.now().format(formatter)
        val logEntry = "$timestamp: $chatId: $message"
        if (!auditFile.exists())
            auditFile.createNewFile()
        auditFile.appendText("\n$logEntry")
        //logger.info(logEntry)
    }
    
    fun getLastLines(count: Int): List<String> {
        return try {
            if (!auditFile.exists()) {
                return listOf("Аудит пуст")
            }
            
            val lines = auditFile.readLines()
                .filter { it.isNotBlank() } // Фильтруем пустые строки
                .takeLast(count)
            
            if (lines.isEmpty()) {
                listOf("Аудит пуст")
            } else {
                lines
            }
        } catch (e: Exception) {
            listOf("Ошибка чтения аудита: ${e.message}")
        }
    }
} 