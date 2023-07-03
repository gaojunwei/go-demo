package com.gjw.go.interceptor

import com.gjw.go.common.utils.MdcUtils
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * MDC 请求ID设置和清除
 */
class MdcInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //设置请求ID
        MdcUtils.initRequestId();
        return super.preHandle(request, response, handler)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        //清除请求ID引用，释放内存，避免内存溢出
        MdcUtils.removeRequestId()
        super.afterCompletion(request, response, handler, ex)
    }
}