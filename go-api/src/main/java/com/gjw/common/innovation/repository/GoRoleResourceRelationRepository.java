package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.GoRoleResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoRoleResourceRelationRepository extends JpaRepository<GoRoleResourceRelation, String>, JpaSpecificationExecutor<GoRoleResourceRelation> {
}