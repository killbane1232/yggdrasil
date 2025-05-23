package ru.arcam.yggdrasil

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class YggdrasilApplication

fun main(args: Array<String>) {
	runApplication<YggdrasilApplication>(*args)
}
