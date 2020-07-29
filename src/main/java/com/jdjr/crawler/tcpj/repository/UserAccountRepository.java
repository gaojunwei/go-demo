package com.jdjr.crawler.tcpj.repository;

import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 操作用户账户表
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {

}