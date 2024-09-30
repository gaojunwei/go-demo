package com.go.controller

import com.go.common.R
import com.go.service.IUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 基于方法鉴权
 */
@RestController
@RequestMapping("auth")
class AuthController(
    private val userService: IUserService
) {

    /**
     * 登陆接口返回token
     */
    @PostMapping("login")
    fun login(@RequestBody loginParam: LoginParam): R<String> {
        val token = userService.login(loginParam)
        return R.ok(token)
    }
}

class LoginParam {
    var username: String? = null
    var password: String? = null
}