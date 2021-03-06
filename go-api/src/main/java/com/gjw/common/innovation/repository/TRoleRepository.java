package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.TRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRoleRepository extends JpaRepository<TRole, Long>,JpaSpecificationExecutor<TRole> {
}