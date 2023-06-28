package com.gjw.go.config

import com.gjw.go.interceptor.MdcInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    /**
     * 拦截器设置
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(MdcInterceptor()) //新增mdc拦截器
    }

    /**
     * 跨域设置
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600)
    }
}