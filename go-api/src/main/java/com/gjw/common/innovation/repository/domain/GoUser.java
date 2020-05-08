package com.gjw.common.innovation.repository.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "go_user")
public class GoUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, columnDefinition = " bigint UNSIGNED")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, columnDefinition = "varchar(200)")
    private String code;

    @Column(name = "account", nullable = false, unique = true, columnDefinition = "varchar(200)")
    private String account;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(200)")
    private String password;

    @Column(name = "email", unique = true, columnDefinition = "varchar(100)")
    private String email;

    @Column(name = "del_flag", nullable = false, columnDefinition = "tinyint(2) UNSIGNED default '0' COMMENT '是否删除(0未删除，1已删除)'")
    private Byte del_flag;

    @Column(name = "created_date", nullable = false, columnDefinition = "datetime")
    private Date created_date;

    @Column(name = "modified_date", nullable = false, columnDefinition = "datetime")
    private Date modified_date;
}
