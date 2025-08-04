package com.go.common.mdc.enums;

import lombok.Getter;

@Getter
public enum MdcEnums {
    TRACE_ID("traceId", "请求ID");

    private final String value;
    private final String desc;

    MdcEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
