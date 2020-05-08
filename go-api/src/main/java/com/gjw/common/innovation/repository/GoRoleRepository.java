package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.GoRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoRoleRepository extends JpaRepository<GoRole, String>, JpaSpecificationExecutor<GoRole> {
}