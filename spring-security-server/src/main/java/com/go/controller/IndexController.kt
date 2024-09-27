package com.go.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class IndexController {

    @GetMapping("test")
    fun index(): String {
        return "Hello World"
    }

    @GetMapping("has_admin")
    fun hasAdmin(): String {
        return "admin 角色有访问权限"
    }
    @GetMapping("has_any")
    fun hasAny(): String {
        return "admin或user 角色有访问权限"
    }
    @GetMapping("has_authority")
    fun hasAuthority(): String {
        return "具有 ‘Authority’权限可以访问"
    }
}