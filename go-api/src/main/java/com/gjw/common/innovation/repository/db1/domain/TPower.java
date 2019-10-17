package com.gjw.common.innovation.repository.db1.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 权限表
 */
@Data
@Entity
public class TPower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String powerValue;
}