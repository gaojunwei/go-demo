package com.gjw.common.enums;

import com.gjw.common.result.BasicResult;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 11:42
 * @Description:
 */
public enum SystemCodeEnums {
    SUCCESS("000000", "success"),
    ERROR("999999", "系统异常,请联系管理员"),
    HttpRequestMethodNotSupportedException("900000", "HTTP请求方法错误"),
    MissingServletRequestParameterException("900001", "缺失请求参数错误"),
    TypeMismatchException("900001", "请求参数类型不正确"),
    JWT_EXPIRED("900002", "TOKEN已过期"),
    JWT_SIGNATURE_FAIL("900003", "TOKEN签名验证失败");

    private String code;
    private String msg;

    SystemCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码及描述
     */
    public void applyValue(BasicResult result) {
        result.setCode(this.getCode());
        result.setMsg(this.getMsg());
    }

    /**
     * 设置错误码及描述
     */
    public BasicResult applyValue() {
        BasicResult result = new BasicResult();
        result.setCode(this.getCode());
        result.setMsg(this.getMsg());
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}