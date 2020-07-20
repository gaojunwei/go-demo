package com.jdjr.crawler.tcpj.common.exception;

import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class AppException extends RuntimeException {
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误描述
     */
    private String msg;
    /**
     * 日志ID
     */
    private String logId;

    public AppException(String code, String msg) {
        super(String.format("%s:%s", code, msg));
        this.code = code;
        this.msg = msg;
    }

    public AppException(String logId, String code, String msg) {
        super(String.format("%s[%s:%s]", logId, code, msg));
        this.logId = logId;
        this.code = code;
        this.msg = msg;
    }
}