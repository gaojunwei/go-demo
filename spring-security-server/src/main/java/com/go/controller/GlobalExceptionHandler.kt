package com.go.controller

import com.go.common.R
import com.go.common.extension.log
import com.go.mapper.domain.User
import io.jsonwebtoken.SignatureException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SignatureException::class)
    fun handleException(e: SignatureException, request: HttpServletRequest): R<Any?> {
        log.error("JWT异常 ${e.message}", e)
        return R.fail()
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleException(e: AccessDeniedException, request: HttpServletRequest): R<Any?> {
        log.error("未授权异常 ${e.message},用户:${(SecurityContextHolder.getContext().authentication.principal as User).loginName},URL:${request.requestURI}")
        return R.fail("资源未授权")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Any?> {
        log.error("系统异常 ${e.message} ", e)
        return R.fail()
    }
}