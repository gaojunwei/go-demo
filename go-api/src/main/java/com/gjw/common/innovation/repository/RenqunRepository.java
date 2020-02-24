package com.gjw.common.innovation.repository;

import com.gjw.common.innovation.repository.domain.Renqun;
import com.gjw.common.innovation.repository.domain.TPower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RenqunRepository extends JpaRepository<Renqun, Long>,JpaSpecificationExecutor<Renqun> {
}