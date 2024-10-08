package com.go.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {
    @GetMapping("to_login")
    fun toLogin(): String {
        println("跳转到登陆页面")
        return "login"
    }

    @GetMapping("index")
    fun index(): String {
        println("跳转到index页面")
        return "index"
    }
}