/*
package com.go.security.provider

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class LoginNameAuthenticationToken(
    private val principal: Any,
    private val credentials: Any,
    authorities: MutableCollection<out GrantedAuthority>?
) : AbstractAuthenticationToken(
    authorities
) {
    init {
        super.setAuthenticated(true)
    }

    override fun getCredentials(): Any {
        return this.credentials
    }

    override fun getPrincipal(): Any {
        return this.principal
    }
}*/
