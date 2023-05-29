package com.gjw.go.common.exception;

import com.gjw.go.common.enums.SysCodeEnums;
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

    public static AppException error() {
        return new AppException(SysCodeEnums.ERROR.getCode(), SysCodeEnums.ERROR.getMsg());
    }

    public static AppException error(String msg) {
        return new AppException(SysCodeEnums.ERROR.getCode(), msg);
    }

    public static AppException success() {
        return new AppException(SysCodeEnums.SUCCESS.getCode(), SysCodeEnums.SUCCESS.getMsg());
    }

    public static AppException success(String msg) {
        return new AppException(SysCodeEnums.SUCCESS.getCode(), msg);
    }

    public AppException(String code, String msg) {
        super(String.format("%s:%s", code, msg));
        this.code = code;
        this.msg = msg;
    }
}