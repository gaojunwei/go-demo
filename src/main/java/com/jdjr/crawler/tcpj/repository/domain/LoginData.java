package com.jdjr.crawler.tcpj.repository.domain;

import com.jdjr.crawler.tcpj.repository.domain.key.UserAccountKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

/**
 * 账户相关的数据信息
 */
@Getter
@Setter
@Entity
@IdClass(UserAccountKey.class)
public class LoginData {
    //账户号
    @Id
    private String account;
    //所属站点
    @Id
    private String site;
    //账号类型
    private Integer type;
    //登录态TOKEN
    @Column(columnDefinition = "longvarchar")
    private String token;
    //登录后返回的cookie信息
    @Column(columnDefinition = "longvarchar")
    private String cookie;
    //创建时间戳
    private Date timeStamp;
    //使用标识(0未使用，1已使用)
    @Column(columnDefinition = "int default 0")
    private Integer isUsed;
    //是否可用(0可用，9不可用)
    @Column(columnDefinition = "int default 0")
    private Integer useful;
    //备注
    @Column(columnDefinition = "longvarchar")
    private String remark;
}
