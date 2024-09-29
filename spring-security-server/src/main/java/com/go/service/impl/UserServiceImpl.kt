package com.go.service.impl

import cn.hutool.core.util.IdUtil
import com.go.controller.LoginParam
import com.go.mapper.domain.User
import com.go.service.IUserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
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
        println("$user 登陆成功")
        // 生成token返回前端
        return IdUtil.simpleUUID().toString()
    }
}