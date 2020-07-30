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
 * @author: gaojunwei
 * @Date: 2018/12/11 20:34
 * @Description: EPL与AP信号强度列表
 */
@Getter
@Setter
@Entity
@IdClass(UserAccountKey.class)
public class UserAccount {
    //账户号
    @Id
    private String account;
    //所属站点
    @Id
    private String site;
    //账号密码
    private String password;
    //账号类型（0：普通账号，1高级账号）
    @Column(columnDefinition = "int default 0")
    private Integer type;
    //是否删除（0：未删除；1：已删除）
    @Column(columnDefinition = "int default 0")
    private Integer delete_flag;
    //修改时间
    private Date modifiedDate;
    //创建时间
    private Date createdDate;
    //异常状态码
    private String code;
    //异常状态码描述
    private String msg;
    //异常状态码修改时间
    private Date code_time;
}