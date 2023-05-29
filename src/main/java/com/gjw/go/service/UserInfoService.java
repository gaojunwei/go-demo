package com.gjw.go.service;

import com.gjw.go.common.enums.FromSourceEnums;
import com.gjw.go.persistence.domain.UserInfo;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
public interface UserInfoService {
    UserInfo getById(FromSourceEnums fromSourceEnums, Long userId);
}
