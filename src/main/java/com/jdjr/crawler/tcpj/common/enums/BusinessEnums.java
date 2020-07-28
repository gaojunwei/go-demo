package com.jdjr.crawler.tcpj.common.enums;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 13:44
 **/
public enum BusinessEnums {
    TCPJ("tcpj"),
    BIHU("bihu");

    private String value;

    BusinessEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
