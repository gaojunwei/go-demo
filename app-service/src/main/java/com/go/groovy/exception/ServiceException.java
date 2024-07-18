package com.go.groovy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public static void assertFalse(boolean expression, String msg) {
        if (expression) {
            throw new ServiceException(msg);
        }
    }

    public static void throwException(String msg) {
        throw new ServiceException(msg);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
