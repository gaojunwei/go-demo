package com.jdjr.crawler.tcpj.repository.domain.key;

import lombok.Data;

import java.io.Serializable;

/**
 * 账号表联合主键
 */
@Data
public class UserAccountKey implements Serializable {
    /**
     * 账户号
     */
    private String account;
    
    /**
     * 所属站点
     */
    private String site;
}