package com.jdjr.crawler.tcpj.service;

public interface BiHuService {
    /**
     * 获取壁虎找票的的登录Token
     */
    String getLoginToken(String url, String account, String password);
}
