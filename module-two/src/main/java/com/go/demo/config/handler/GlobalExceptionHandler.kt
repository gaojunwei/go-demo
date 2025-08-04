package com.go.demo.config.handler

import cn.hutool.http.HttpStatus
import com.go.common.exception.ServiceException
import com.go.common.extension.log
import com.go.common.response.R
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun handleException(ex: ServiceException, request: HttpServletRequest): R<Unit> {
        log.error("请求地址'{}',ServiceException异常:", request.requestURI, ex)
        return R.fail(code = ex.code, msg = ex.msg)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Unit> {
        log.error("请求地址'{}',Exception异常:", request.requestURI, e)
        return R.fail(code = HttpStatus.HTTP_INTERNAL_ERROR, msg = "系统异常，请联系管理员")
    }
}