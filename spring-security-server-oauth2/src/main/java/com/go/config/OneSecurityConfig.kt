/*
package com.go.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

*/
/**
 * 基于请求鉴权
 *//*

@Configuration
@EnableWebSecurity(debug = true)
class OneSecurityConfig {
    // 密码加密器
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

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

    // 定义一个过滤器链，该链能够与 HttpServletRequest. 匹配，以确定它是否适用于该请求
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // 关闭csrf机制
        http.csrf { it.disable() }
        // 配置拦截方式-基于请求的授权
        http.authorizeHttpRequests { auth ->
            // to_login 接口允许任意访问（未登录也可访问）
            auth.requestMatchers("/to_login").permitAll()
                // has_admin 接口，登陆用户必须有 admin 角色
                .requestMatchers("/has_admin").hasRole("admin")
                // has_any 接口，登陆用户必须有 admin 或 user 角色
                .requestMatchers("/has_any").hasAnyRole("admin", "user")
                // has_authority 接口，登陆用户必须有 'test:show' 权限
                .requestMatchers("/has_authority").hasAuthority("test:show")
                // 其他请求 登陆即可访问
                .anyRequest().authenticated()
        }
        // 默认的登陆配置
        //http.formLogin(Customizer.withDefaults())
        // 覆盖原有的登陆配置
        http.formLogin {
            it.loginPage("/to_login")//跳转到指定登陆页
                .loginProcessingUrl("/doLogin")//处理前端的请求与form表单一致
                .usernameParameter("username") //用户名
                .passwordParameter("password") //密码
                .defaultSuccessUrl("/index") //
        }
        return http.build()
    }
}
*/
