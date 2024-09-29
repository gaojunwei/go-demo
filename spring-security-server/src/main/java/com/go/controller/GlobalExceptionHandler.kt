package com.go.controller

import com.go.common.R
import com.go.common.extension.log
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Any?> {
        log.error("系统异常 ${e.message} $e")
        return R.fail()
    }
}