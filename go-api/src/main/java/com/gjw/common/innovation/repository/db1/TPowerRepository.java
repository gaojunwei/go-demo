package com.gjw.common.innovation.repository.db1;

import com.gjw.common.innovation.repository.db1.domain.TPower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TPowerRepository extends JpaRepository<TPower, Long>,JpaSpecificationExecutor<TPower> {
}