package com.gjw.innovation.common.utils;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 唯一标识工具类
 */
public class UuidUtils {

    /**
     * 随机生成UUID
     */
    public static synchronized String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 根据字符串生成固定UUID
     */
    public static synchronized String getUUID(String str) {
        if (!StringUtils.isEmpty(str)) {
            UUID uuid = UUID.nameUUIDFromBytes(str.getBytes());
            return uuid.toString().replace("-", "");
        } else {
            return "";
        }
    }
}
