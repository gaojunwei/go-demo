package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户表
 */
@Data
@Entity
@Table(name = "t_user", uniqueConstraints = {@UniqueConstraint(columnNames="user_account")})
public class TUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name",nullable = false)
    private String userName;
    @Column(name = "user_account",nullable = false)
    private String userAccount;
    @Column(name = "user_pwd",nullable = false)
    private String userPwd;
}