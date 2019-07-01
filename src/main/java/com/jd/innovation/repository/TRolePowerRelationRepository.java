package com.jd.innovation.repository;

import com.jd.innovation.repository.domain.TRolePowerRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRolePowerRelationRepository extends JpaRepository<TRolePowerRelation, Long>,JpaSpecificationExecutor<TRolePowerRelation> {
}