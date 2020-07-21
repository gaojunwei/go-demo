package com.jdjr.crawler.tcpj.schedule.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/20 14:22
 **/
@Setter
@Getter
public class AccountLogInfo {
    /**
     * 账号
     */
    private String account;
    /**
     * 获取到的token值
     */
    private String token;
    /**
     * 详细日志记录
     */
    List<String> logs;
}
