package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统权限表
 */
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission implements Serializable {

    /**
     * 权限ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * URL
     */
    private String url;

}
