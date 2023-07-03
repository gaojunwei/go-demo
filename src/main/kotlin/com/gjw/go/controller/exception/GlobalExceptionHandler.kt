package com.gjw.go.controller.exception

import com.gjw.go.common.exception.AppException
import com.gjw.go.common.inline.log
import com.gjw.go.common.result.BasicRespDto
import com.gjw.go.common.utils.MdcUtils
import org.springframework.validation.BindException
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
        log.error("业务异常拦截 请求地址：[{}] {},异常描述：{}", request.method, request.requestURI, e.toString())
        return BasicRespDto.error(code = e.code, msg = e.msg).apply {
            sn = MdcUtils.getRequestId()
        }
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException, request: HttpServletRequest): BasicRespDto {
        log.error("参数校验未通过 请求地址：[{}] {},异常描述：{}", request.method, request.requestURI, e.toString())
        return BasicRespDto.error(code = null, msg = e.bindingResult.allErrors[0].defaultMessage).apply {
            sn = MdcUtils.getRequestId()
        }
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): BasicRespDto {
        log.error("系统异常 请求地址：[{}] {}", request.method, request.requestURI, e)
        return BasicRespDto.error(null, null).apply {
            sn = MdcUtils.getRequestId()
        }
    }
}