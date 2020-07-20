package com.jdjr.crawler.tcpj.service;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/15 14:20
 **/
public interface TCPJService {
    /**
     * 获取同城票据的登录Token
     *
     * @param url
     * @param account
     * @param password
     * @return
     */
    String getLoginToken(String url, String account, String password);
}
