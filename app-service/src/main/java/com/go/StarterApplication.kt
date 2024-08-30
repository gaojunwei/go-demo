package com.go

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class StarterApplication

fun main(args: Array<String>) {
    SpringApplication(StarterApplication::class.java).run(*args)
    println("my-flow启动成功!!!")
}
