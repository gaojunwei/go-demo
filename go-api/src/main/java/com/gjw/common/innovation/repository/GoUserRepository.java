package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.GoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoUserRepository extends JpaRepository<GoUser, String>, JpaSpecificationExecutor<GoUser> {
}