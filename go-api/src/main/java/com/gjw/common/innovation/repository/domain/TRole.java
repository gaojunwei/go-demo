package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 角色表
 */
@Data
@Entity
public class TRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roleName;
}