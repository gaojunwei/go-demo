package com.gjw.common.utils;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.exception.AppException;

public class ExceptionUtils {

    public static AppException fail() {
        return new AppException(SystemCodeEnums.FAIL.getCode(), SystemCodeEnums.FAIL.getMsg());
    }

    public static AppException fail(String msg) {
        return new AppException(SystemCodeEnums.FAIL.getCode(), msg == null ? SystemCodeEnums.FAIL.getMsg() : msg);
    }

    public static AppException notLogin() {
        return new AppException(SystemCodeEnums.JWT_EXPIRED.getCode(), SystemCodeEnums.JWT_EXPIRED.getMsg());
    }
}
