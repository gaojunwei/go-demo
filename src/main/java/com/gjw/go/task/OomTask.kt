package com.gjw.go.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class OomTask {

    private val memoryHog: MutableList<ByteArray> = ArrayList()

    // 每秒执行一次
    @Scheduled(fixedRate = 1000)
    fun consumeMemory() {
        for (i in 0..9999) {
            val block = ByteArray(1024 * 1024) // 每次分配1MB
            memoryHog.add(block)
            println("正在分配内存...${i}")
            TimeUnit.MILLISECONDS.sleep(100)
        }
    }
}