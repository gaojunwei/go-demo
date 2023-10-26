package com.gjw.common.enums;

public enum CacheKey {
    openid("openid", "openid"),
    sessionKey("sessionKey", "sessionKey"),
    token("token", "token"),
    authorization("authorization", "token"),
    ;
    private String value;
    private String desc;

    CacheKey(String value, String desc) {
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
