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
            //todo gjw 查询用户权限
            //val authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin")
            user.perms.add("test:show")
            user.perms.add("test:show1")
            user.perms.add("test:show2")

            return user
        } ?: throw UsernameNotFoundException("$username:用户不存在")
    }
}