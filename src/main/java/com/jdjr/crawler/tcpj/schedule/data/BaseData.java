package com.jdjr.crawler.tcpj.schedule.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/28 10:54
 **/
@Setter
@Getter
public class BaseData {
    /**
     * 手机号账号
     */
    private String phone;
    /**
     * 登录态Token
     */
    private String token;
    /**
     * 登录态Token的创建时间
     */
    private Date creatTime;
    /**
     * 使用标识(0未使用，1已使用)
     */
    private int isUsed = 0;
}
