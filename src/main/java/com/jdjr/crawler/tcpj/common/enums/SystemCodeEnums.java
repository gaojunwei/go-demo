package com.jdjr.crawler.tcpj.common.enums;

import com.jdjr.crawler.tcpj.common.result.BasicResult;

/**
 * 系统状态码枚举类
 */
public enum SystemCodeEnums {
    SUCCESS("0", "success"),
    ERROR("9", "exception");

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