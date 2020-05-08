package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.GoResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoResourceRepository extends JpaRepository<GoResource, String>, JpaSpecificationExecutor<GoResource> {
}