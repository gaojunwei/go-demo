package com.gjw.go.controller.exception

import com.gjw.go.common.exception.AppException
import com.gjw.go.common.log.log
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.common.utils.MdcUtils
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(AppException::class)
    fun handleAppException(e: AppException, request: HttpServletRequest): BasicRespDto {
        val requestURI = request.requestURI
        log.error("业务异常拦截 请求地址：{},异常描述：{}", requestURI, e.toString())
        return BasicRespDto.error(code = e.code, msg = e.msg).apply {
            sn = MdcUtils.getRequestId()
        }
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): BasicRespDto {
        val requestURI = request.requestURI
        log.error("系统异常 请求地址：{}", requestURI, e)
        return BasicRespDto.error(null, null).apply {
            sn = MdcUtils.getRequestId()
        }
    }
}