package com.gjw.go.common.enums;

import com.gjw.go.common.result.BasicResponseDto;
import lombok.Getter;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 11:42
 * @Description:
 */
@Getter
public enum SysCodeEnums {
    SUCCESS("0", "success"),
    ERROR("9", "系统异常,请联系管理员");

    private String code;
    private String msg;

    SysCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码及描述
     *
     * @param result
     */
    public void applyValue(BasicResponseDto result) {
        result.setCode(this.getCode());
        result.setMsg(this.getMsg());
    }
}