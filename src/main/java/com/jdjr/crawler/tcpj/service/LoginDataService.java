package com.jdjr.crawler.tcpj.service;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 13:46
 **/
public interface LoginDataService {

    /**
     * 修改token为无效状态
     *
     * @param account
     * @param site
     * @param remark
     */
    void invalidToken(String account,String site,String remark);

    /**
     * 修改token为有效状态
     *
     * @param account
     * @param site
     * @param remark
     */
    void validToken(String account,String site,String remark);
}
