package com.jdjr.crawler.tcpj.repository;

import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 操作用户账户表
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {
    /**
     * 维护状态码信息-用于检测命中风控信息
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_ACCOUNT  SET CODE=?3,msg=?4,code_time=?5 WHERE SITE=?1 AND account=?2", nativeQuery = true)
    int updateAccountCodeInfo(String site, String account, String code, String msg, Date date);
}