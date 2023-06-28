package com.gjw.go

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoApp

fun main(args: Array<String>) {
    runApplication<GoApp>(*args)
}