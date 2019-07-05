package com.jd.innovation.common.enums;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 11:42
 * @Description:
 */
public enum SystemCodeEnums {
    SUCCESS("000000","success"),
    ERROR("999999","系统异常,请联系管理员"),
    HttpRequestMethodNotSupportedException("900000","HTTP请求方法错误"),
    MissingServletRequestParameterException("900001","缺失请求参数错误"),
    TypeMismatchException("900001","请求参数类型不正确"),
    JWT_EXPIRED("900002", "TOKEN已过期"),
    JWT_SIGNATURE_FAIL("900003", "TOKEN签名验证失败");

    private String code;
    private String msg;
    SystemCodeEnums(String code,String msg){
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