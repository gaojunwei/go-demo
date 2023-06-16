package com.test.service.kt.xc

import com.test.service.kt.ext.log
import kotlinx.coroutines.*

//sampleStart
fun main() = runBlocking {
    doWorld01()
    this.log.info("结束")
}

suspend fun doWorld01() = runBlocking {  // this: CoroutineScope
    launch {
        delay(2000L)
        this.log.info("World2")
    }
    launch {
        delay(1000L)
        this.log.info("World1")
    }
    this.log.info("Hello")
}