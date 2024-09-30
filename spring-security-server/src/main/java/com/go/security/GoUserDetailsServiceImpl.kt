package com.go.security

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.go.mapper.domain.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class GoUserDetailsServiceImpl : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        KtQueryChainWrapper(User::class.java).eq(User::loginName, username).one()?.let { user ->
            println("UserDetailsService 下 loadUserByUsername")
            //todo gjw 查询用户权限模拟数据
            user.perms.addAll(MockData.getPerms())
            return user
        } ?: throw UsernameNotFoundException("$username:用户不存在")
    }
}