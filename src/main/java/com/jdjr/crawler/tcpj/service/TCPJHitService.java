package com.jdjr.crawler.tcpj.service;

import com.jdjr.crawler.tcpj.common.result.BasicResult;

/**
 * 检测同城账号是否命中风控
 *
 * @Author gaojunwei
 * @Date 2020/7/30 10:14
 **/
public interface TCPJHitService {

    /**
     * 检测同城账号是否命中风控
     */
    void checkHit(String taskId);

    /**
     * 检测是否命中反爬措施
     * 返回接口响应的code和msg
     */
    BasicResult checkFP(String url, String token);
}
