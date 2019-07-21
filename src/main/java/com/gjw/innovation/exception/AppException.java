package com.gjw.innovation.exception;

/**
 * 自定义异常
 */
public class AppException extends RuntimeException{
    private String code;
    private String msg;

    public AppException(String code,String msg) {
        super(String.format("%s:%s",code,msg));
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}