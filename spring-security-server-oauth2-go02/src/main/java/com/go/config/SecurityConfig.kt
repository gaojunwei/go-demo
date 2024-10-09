package com.go.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

/**
 * 动态权限鉴权
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {
    // 自定义用户名和密码
    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        var user1 = User.withUsername("admin")
            .password(passwordEncoder.encode("tiger"))
            .roles("admin", "user")
            .authorities("test:show")
            .build()
        var user2 = User.withUsername("gjw")
            .password(passwordEncoder.encode("tiger"))
            .roles("user")
            .build()
        return InMemoryUserDetailsManager().apply {
            createUser(user1)
            createUser(user2)
        }
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
            auth.requestMatchers("/oauth/notify", "/to_login").permitAll()
                .anyRequest().authenticated()
        }
        // 使用默认登陆页面
        http.formLogin(Customizer.withDefaults())
        // 开启oauth2登陆
        http.oauth2Login(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(giteeClientRegistration())
    }

    // 配置gitee的授权登陆信息
    private fun giteeClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("gitee")
            .clientId("4acc995f3b994fe11d5cca9c4d2a942211afd732c391fc6da739bb5274dd22af")
            .clientSecret("55b243f5af4f8238d23c7233d6b3263aaa6ade6b7cd67b43026c9cf7a7169b09")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:9002/oauth/notify")
            .scope("user_info")
            .authorizationUri("https://gitee.com/oauth/authorize")
            .tokenUri("https://gitee.com/oauth/token")
            .userInfoUri("https://gitee.com/api/v5/user")
            .userNameAttributeName("name")
            .build()
    }
}