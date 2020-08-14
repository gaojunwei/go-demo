package com.jdjr.crawler.tcpj.repository;

import com.jdjr.crawler.tcpj.repository.domain.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作用户账户表
 */
public interface TaskLogRepository extends JpaRepository<TaskLog, String>, JpaSpecificationExecutor<TaskLog> {
}