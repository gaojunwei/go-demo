package com.gjw.common.enums;

/**
 * 主要功能描述：
 */
public enum MdcEnums {
    RequestId("requestId", "请求ID");

    private String value;
    private String desc;

    MdcEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
