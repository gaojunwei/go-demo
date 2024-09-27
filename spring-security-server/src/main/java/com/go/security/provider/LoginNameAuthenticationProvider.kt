package com.go.security.provider

import com.go.security.UserDetailsServiceImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication


class LoginNameAuthenticationProvider(
    private val userDetailsService: UserDetailsServiceImpl
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        // 从认证令牌中获取登录名和密码
        val loginName = authentication!!.principal as? String
        val password = authentication.credentials as? String
        // 根据登录名加载用户详细信息
        val userDetails = userDetailsService.loadUserByUsername(loginName)
        // 验证密码是否匹配
        if (password != userDetails.password) {
            throw BadCredentialsException("密码错误")
        }
        // 创建并返回认证成功的对象
        return LoginNameAuthenticationToken(userDetails, password!!, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        // 当 LoginNameAuthenticationToken 认证时，匹配该类
        return LoginNameAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}