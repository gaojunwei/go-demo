package com.gjw.common.innovation.repository.db2.dao;

import com.gjw.common.innovation.repository.db2.dao.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("secondaryTUserRepository")
public interface TUserRepository extends JpaRepository<TUser, Long>,JpaSpecificationExecutor<TUser> {
}