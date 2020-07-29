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
public class TaskLog {
    //数据库自增主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    //站点
    private String site;
    //日志描述
    @Column(columnDefinition = "longvarchar")
    private String logStr;
}
