package com.gjw.go.common.enums;

import lombok.Getter;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Getter
public enum FromSourceEnums {
    A("A", "来自A星球"),
    B("B", "来自B星球"),
    ;
    private String value;
    private String desc;

    FromSourceEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
