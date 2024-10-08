package com.go.security.manager

import com.go.security.MockData
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * 自定义认证管理器，判断请求路径是否有权限访问,
 * 注意：方法无需注解
 */
@Component
class GoAuthorizationManager : AuthorizationManager<RequestAuthorizationContext> {
    override fun check(
        authentication: Supplier<Authentication>,
        requestAuthorizationContext: RequestAuthorizationContext
    ): AuthorizationDecision? {
        // 获取请求路径
        val uri = requestAuthorizationContext.request.requestURI
        val url = requestAuthorizationContext.request.requestURL
        println("uri ====> $uri")
        println("url ====> $url")
        // 排除不需要认证的的请求路径
        if ("/auth/login" == uri || "logout" == uri || "/error" == uri) {
            return AuthorizationDecision(true)
        }
        // todo 根据uri获取路径权限，查询数据库或缓存得到
        val menuPerm = MockData.goAuthorizationManagerMenuData()
        if (!menuPerm.contains(uri)) {
            return AuthorizationDecision(false)
        }
        val perm = menuPerm[uri]
        if (perm == "") {
            return AuthorizationDecision(true)
        }
        // 与用户权限集合做判断
        val authoritySet = authentication.get().authorities.map { it.authority }.toSet()
        return AuthorizationDecision(authoritySet.contains(perm))
    }
}