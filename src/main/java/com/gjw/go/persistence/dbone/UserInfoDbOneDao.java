package com.gjw.go.persistence.dbone;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjw.go.persistence.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Mapper
@DS("db_one")
public interface UserInfoDbOneDao extends BaseMapper<UserInfo> {
}
