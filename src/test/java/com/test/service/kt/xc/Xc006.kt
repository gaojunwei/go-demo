package com.test.service.kt.xc

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    repeat(50_000) { // 启动大量的协程
        launch {
            delay(5000L)
            print(".")
        }
    }
}