package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.GoUserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoUserRoleRelationRepository extends JpaRepository<GoUserRoleRelation, String>, JpaSpecificationExecutor<GoUserRoleRelation> {
}