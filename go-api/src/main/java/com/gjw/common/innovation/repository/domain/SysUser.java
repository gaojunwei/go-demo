package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统账户表
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账户号
     */
    @Column(name = "user_code")
    private String userCode;

    /**
     * 账户昵称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 账户密码
     */
    @Column(name = "user_pwd")
    private String userPwd;
}
