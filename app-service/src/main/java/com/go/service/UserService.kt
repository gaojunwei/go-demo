package com.go.service

import org.springframework.stereotype.Service

@Service
class UserService {

    fun dowork1() {
        println("无参 无返回 方法")
    }

    fun dowork2(value: String) {
        println("有参 无返回 方法 $value")
    }

    fun dowork3(value: String): String {
        println("有参 无返回 方法 $value")
        return "hello $value"
    }
}