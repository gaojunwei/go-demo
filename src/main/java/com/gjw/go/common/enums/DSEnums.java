package com.gjw.go.common.enums;

import lombok.Getter;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Getter
public enum DSEnums {
    db_two("db_two", "多数据源之数据库2");

    private String value;
    private String desc;

    DSEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}
