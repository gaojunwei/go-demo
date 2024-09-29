package com.go.config

import jakarta.annotation.Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * 动态权限鉴权
 */
@Configuration
@EnableWebSecurity
class ThreeSecurityConfig {

    @Resource
    private lateinit var userDetailsService: UserDetailsService

    /**
     * AuthenticationManager：负责认证
     * DaoAuthenticationProvider：负责将userDetailsService、passwordEncoder融合起来送到AuthenticationManager中
     */
    @Bean
    fun authenticationManager(passwordEncoder: PasswordEncoder): AuthenticationManager {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        // 关联使用的密码加密器
        provider.setPasswordEncoder(passwordEncoder)
        // 将provider放置进AuthenticationManager中
        return ProviderManager(provider)
    }

    // 密码加密器
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }


    // 定义一个过滤器链，该链能够与 HttpServletRequest. 匹配，以确定它是否适用于该请求
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // 关闭csrf机制
        http.csrf { it.disable() }
        // 配置拦截方式-基于请求的授权
        http.authorizeHttpRequests { auth ->
            // to_login 接口允许任意访问（未登录也可访问）
            auth.requestMatchers("/auth/login").permitAll()
                // 其他请求 登陆即可访问
                .anyRequest().authenticated()
        }
        return http.build()
    }
}