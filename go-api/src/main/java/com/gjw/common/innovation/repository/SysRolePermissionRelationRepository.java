package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.SysRolePermissionRelation;
import com.gjw.common.innovation.repository.domain.key.SysRolePermissionRelationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysRolePermissionRelationRepository extends JpaRepository<SysRolePermissionRelation, SysRolePermissionRelationKey>, JpaSpecificationExecutor<SysRolePermissionRelation> {
}