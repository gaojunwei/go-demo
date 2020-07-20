package com.jdjr.crawler.tcpj.exception;

import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import com.jdjr.crawler.tcpj.common.result.BasicResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 其他异常
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public BasicResult appExceptionHandler(AppException e) {
        logger.error("{}", e.getMessage(), e);
        BasicResult result = new BasicResult();
        result.setCode(e.getCode());
        result.setMsg(e.getMsg());
        return result;
    }

    /**
     * 其他异常
     */
    @ExceptionHandler
    @ResponseBody
    public BasicResult allExceptionHandler(Exception e) {
        logger.info("所有异常");
        logger.error("{}", e.getMessage(), e);
        return SystemCodeEnums.ERROR.applyValue();
    }
}
