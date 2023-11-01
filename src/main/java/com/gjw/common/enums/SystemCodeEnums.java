package com.gjw.common.enums;

import com.gjw.common.result.BasicResult;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 11:42
 * @Description:
 */
public enum SystemCodeEnums {
    SUCCESS("0", "success"),
    FAIL("9", "操作失败"),
    JWT_EXPIRED("-1", "TOKEN已过期");

    private final String code;
    private final String msg;

    SystemCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码及描述
     *
     * @param result
     */
    public void applyValue(BasicResult result) {
        result.setCode(this.getCode());
        result.setMsg(this.getMsg());
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}