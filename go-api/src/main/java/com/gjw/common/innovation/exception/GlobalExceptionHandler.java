package com.gjw.common.innovation.exception;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.exception.AppException;
import com.gjw.common.result.BasicResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * http请求的方法不正确
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public BasicResult httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(),e);
        return BasicResult.instance(SystemCodeEnums.HttpRequestMethodNotSupportedException.getCode(),SystemCodeEnums.HttpRequestMethodNotSupportedException.getMsg());
    }

    /**
     * 请求参数不全
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BasicResult missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(),e);
        return BasicResult.instance(SystemCodeEnums.MissingServletRequestParameterException.getCode(),SystemCodeEnums.MissingServletRequestParameterException.getMsg());
    }

    /**
     * 请求参数类型不正确
     */
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public BasicResult typeMismatchExceptionHandler(TypeMismatchException e) {
        logger.error(e.getMessage(),e);
        return BasicResult.instance(SystemCodeEnums.TypeMismatchException.getCode(),SystemCodeEnums.TypeMismatchException.getMsg());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public BasicResult allExceptionHandler(AppException e){
        logger.error(e.getMessage(),e);
        return BasicResult.instance(e.getCode(),e.getMsg());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler
    @ResponseBody
    public BasicResult allExceptionHandler(Exception e) {
        logger.error(e.getMessage(),e);
        return BasicResult.instance(SystemCodeEnums.ERROR.getCode(),SystemCodeEnums.ERROR.getMsg());
    }
}
