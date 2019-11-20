package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.SysUserRoleRelation;
import com.gjw.common.innovation.repository.domain.key.SysUserRoleRelationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysUserRoleRelationRepository extends JpaRepository<SysUserRoleRelation, SysUserRoleRelationKey>, JpaSpecificationExecutor<SysUserRoleRelation> {
}