package com.ecust.test;

import lombok.ToString;

import java.io.Serializable;

/**
 * 序列化对象需要实现 Serializable 接口
 */
@ToString
public class UserInfo implements Serializable {


    //静态属性不参与序列化
    public static Integer count = 101;

    private Integer age;
    private String name;
    //瞬态[ˈtrænziənt] 修饰的属性不参与序列化
    private transient String hby;

    public UserInfo(Integer age, String name, String hby) {
        this.age = age;
        this.name = name;
        this.hby = hby;
    }
}
