package com.gjw.common.innovation.repository.db1.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户表
 */
@Data
@Entity
public class TUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userName;
}