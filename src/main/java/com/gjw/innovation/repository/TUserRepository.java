package com.gjw.innovation.repository;

import com.gjw.innovation.repository.domain.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TUserRepository extends JpaRepository<TUser, Long>,JpaSpecificationExecutor<TUser> {
}