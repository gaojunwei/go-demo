package com.gjw.common.innovation.repository;

import com.gjw.common.BasicMybatisDao;
import com.gjw.common.innovation.repository.domain.TUser;
import com.gjw.common.innovation.repository.domain.TUserExample;
import org.springframework.stereotype.Repository;

/**
 * @author gaojunwei
 * @date 2019-11-04 14:08
 **/
@Repository
public interface TUserDao extends BasicMybatisDao<TUser,TUserExample> {
}
