package com.test.service.kt.xc

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
//sampleStart
    val job = launch { // launch a new coroutine and keep a reference to its Job
        delay(2000L)
        println("World!")
    }
    println("Hello")
    job.join() // 等待指导 子协程执行完成
    println("Done")
//sampleEnd
}