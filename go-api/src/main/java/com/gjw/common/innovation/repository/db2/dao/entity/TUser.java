package com.gjw.common.innovation.repository.db2.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户表
 */
@Data
@Entity
@Table(name = "t_user")
public class TUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userName;
}