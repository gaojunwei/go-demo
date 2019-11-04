package com.gjw.common.innovation.repository.domain;

import lombok.Data;

/**
 * 
 * @author MyBatis Generator
 * @date 2019-11-04
 */
@Data
public class TUser {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;
}