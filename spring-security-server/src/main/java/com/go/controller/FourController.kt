package com.go.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 自定义认证管理测试
 */
@RestController
@RequestMapping("four")
class FourController {

    @GetMapping("/m_1")
    fun m1(): String {
        return "自定义认证管理测试 m_1"
    }

    @GetMapping("/m_2")
    fun m2(): String {
        return "自定义认证管理测试 m_2"
    }
}