package com.go.security

import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateUtil
import org.junit.jupiter.api.Test

class JwtUtilsTest {
    @Test
    fun generateToken() {
        val token = JwtUtils.createToken(mutableMapOf("username" to "gjw", "perms" to "admin"))
        println("生成jwt token = $token")
        println()
        val result = JwtUtils.parseToken(token)
        println("jwt 解析结果：")
        println("username = ${result["username"]}")
        println("perms = ${result["perms"]}")
        println("jwt 生成时间 = ${DateUtil.format(result.issuedAt, DatePattern.NORM_DATETIME_FORMAT)}")
        println("jwt 过期时间 = ${DateUtil.format(result.expiration, DatePattern.NORM_DATETIME_FORMAT)}")
    }
}