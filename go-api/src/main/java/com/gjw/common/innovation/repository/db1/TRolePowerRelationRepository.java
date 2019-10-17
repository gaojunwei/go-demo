package com.gjw.common.innovation.repository.db1;

import com.gjw.common.innovation.repository.db1.domain.TRolePowerRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRolePowerRelationRepository extends JpaRepository<TRolePowerRelation, Long>,JpaSpecificationExecutor<TRolePowerRelation> {
}