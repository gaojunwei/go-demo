package com.jdjr.crawler.tcpj.repository;

import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 操作用户账户表
 */
public interface LoginDataRepository extends JpaRepository<LoginData, String>, JpaSpecificationExecutor<LoginData> {

    /**
     * 按站点设置全部账号为未使用状态
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE LOGIN_DATA  SET IS_USED =0 WHERE SITE=?1 AND TYPE=?2 AND useful=0", nativeQuery = true)
    int unUsedSet(String site, Integer type);

    /**
     * 删除过期数据
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM LOGIN_DATA WHERE SITE =?1 and TIME_STAMP<?2", nativeQuery = true)
    void deleteExpiredData(String site, Date timePoint);

    /**
     * 设置Token的有效状态
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE LOGIN_DATA  SET useful=?1,remark=?2 WHERE account=?3 AND SITE=?4", nativeQuery = true)
    int updateUseful(Integer useful, String remark, String account, String site);
}