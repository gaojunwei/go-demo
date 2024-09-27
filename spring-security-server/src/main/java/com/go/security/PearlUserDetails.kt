package com.go.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 自定义用户信息 - UserDetails
 */
class PearlUserDetails(
    private val userId: Long,
    private val userName: String,
    private val password: String,
    private val loginName: String,
    private val authorities: List<GrantedAuthority>,
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return loginName
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}