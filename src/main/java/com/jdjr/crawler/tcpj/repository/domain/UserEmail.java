package com.jdjr.crawler.tcpj.repository.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/29 15:15
 **/
@Getter
@Setter
@Entity
public class UserEmail {
    //数据库自增主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //邮箱地址
    private String email;
    @Column
    private String logStr;
    //删除状态（0未删除 1已删除）
    @Column(columnDefinition = "int default 0")
    private Integer delete_flag;
}
