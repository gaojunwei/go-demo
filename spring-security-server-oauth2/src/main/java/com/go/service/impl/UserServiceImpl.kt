package com.go.service.impl

import com.go.controller.LoginParam
import com.go.mapper.domain.User
import com.go.security.JwtUtils
import com.go.service.IUserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val authenticationManager: AuthenticationManager
) : IUserService {
    /**
     * 这个认证就需要SpringSecurity帮我们实现
     */
    override fun login(param: LoginParam): String {
        // 传入用户名和密码
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(param.username, param.password)
        // 实现登录逻辑，此时就会调用loadUserByUsername方法
        //返回的Authentication其实就是UserDetails
        val authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken)
        val user = authenticate.principal as User
        println("********> ${user.loginName} 登陆成功")
        // 生成token
        return JwtUtils.createToken(
            mutableMapOf(
                "userId" to user.userId!!,
                "loginName" to user.loginName!!,
                "perms" to user.perms
            )
        )
    }
}