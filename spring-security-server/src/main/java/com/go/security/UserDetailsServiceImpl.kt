package com.go.security

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.go.mapper.domain.User
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceImpl : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        KtQueryChainWrapper(User::class.java).eq(User::loginName, username).one()?.let { user ->
            val authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin")
            return PearlUserDetails(
                userId = user.userId!!,
                userName = user.userName!!,
                password = user.password!!,
                loginName = user.loginName!!,
                authorities = authorityList
            )
        } ?: throw UsernameNotFoundException("$username:用户不存在")
    }
}