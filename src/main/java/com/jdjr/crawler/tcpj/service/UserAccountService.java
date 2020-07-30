package com.jdjr.crawler.tcpj.service;

import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;

import java.util.Date;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/29 10:57
 **/
public interface UserAccountService {
    /**
     * 获取指定站点下可用账号信息
     */
    List<UserAccount> getAll(BusinessEnums siteEnum);

    /**
     * 保存账号Token信息
     */
    void saveToken(LoginData param);

    /**
     * 获取Token信息
     */
    LoginData getToken(BusinessEnums siteEnum, Integer phoneType);

    /**
     * 获取Token信息
     */
    List<LoginData> getAllToken(BusinessEnums siteEnum);

    /**
     * 保存日志记录
     */
    void saveTaskLog(String site, String logStr);

    /**
     * 清除过期的Token数据
     */
    void clearExpiredData(BusinessEnums businessEnums, Date timePoint);

    /**
     * 维护账号code信息（用于监测命中风控）
     */
    int updateAccountCodeInfo(String site, String account, String code, String msg);

    /**
     * 根据条件查询账户信息
     */
    List<UserAccount> listUserByCon(UserAccount userAccount);
}
