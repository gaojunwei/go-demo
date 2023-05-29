package com.gjw.go.persistence.dbtwo;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjw.go.common.enums.DSEnums;
import com.gjw.go.persistence.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Mapper
@DS("db_two")
public interface UserInfoDbTwoDao extends BaseMapper<UserInfo> {
}
