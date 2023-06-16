package com.test.service.kt.xc


import kotlinx.coroutines.*

//sampleStart
fun main() = runBlocking { // this: CoroutineScope
    launch { doWorld2() }
    println("Hello")
}

// this is your first suspending function
suspend fun doWorld2() {
    delay(1000L)
    println("World!")
}
//sampleEnd