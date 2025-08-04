package com.go.demo.config.interceptor

import com.go.common.mdc.MdcUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

/**
 * MDC拦截器
 */
class MdcInterceptor : HandlerInterceptor {
    /**
     * 预处理请求，初始化请求ID
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        MdcUtils.initTraceId()
        return true
    }

    /**
     * 请求完成后，移除请求ID
     */
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        MdcUtils.removeTraceId()
    }
}
