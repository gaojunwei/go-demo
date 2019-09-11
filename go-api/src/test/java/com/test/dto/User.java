package com.test.dto;

import lombok.Data;

/**
 * @author: gaojunwei
 * @Date: 2018/12/6 14:59
 * @Description:
 */
@Data
public class User {
    private String name;
    private long createTime;
    private int level;

    public User(String name, long createTime, int level) {
        this.name = name;
        this.createTime = createTime;
        this.level = level;
    }
}