package com.go.security.filter

import com.go.mapper.domain.User
import com.go.security.JwtUtils
import com.go.security.MockData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 捕获请求头中的token，解析token，获取用户信息
 *
 * 1、获取到用户信息，告知 springSecurity ，springSecurity 会根据访问的接口进行鉴权；
 * 2、告知springSecurity 就是使用Authentication告知框架，springSecurity 会将信息放到SecurityContextHolder中->SecurityContextHolder;
 *
 */
@Component
class JwtAuthFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(MockData.TOKEN_HEADER)
        //login接口不需要校验token，直接放行，因为后边还有其他的过滤器
        if (token == null) {
            doFilter(request, response, filterChain)
            return
        }
        //解析token
        val data = JwtUtils.parseToken(token)
        //获取到用户信息, 放入到SecurityContext中
        val user = User().apply {
            loginName = data.get("loginName", String::class.java)
            userId = data["userId"].toString().toLong()
            perms = (data["perms"] as MutableList<String>).toMutableSet()
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        SecurityContextHolder.getContext().authentication = authenticationToken
        // 放行
        doFilter(request, response, filterChain)
    }
}