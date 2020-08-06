package com.jdjr.crawler.tcpj.repository;

import com.jdjr.crawler.tcpj.repository.domain.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * email表
 */
public interface UserEmailRepository extends JpaRepository<UserEmail, Integer>, JpaSpecificationExecutor<UserEmail> {

}