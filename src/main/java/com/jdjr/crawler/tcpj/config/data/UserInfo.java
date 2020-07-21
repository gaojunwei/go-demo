package com.jdjr.crawler.tcpj.config.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/20 13:59
 **/
@Getter
@Setter
public class UserInfo {
    /**
     * 账户号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号类型(0:爬列表数据,1:爬票面数据)
     */
    private Integer type;
}
