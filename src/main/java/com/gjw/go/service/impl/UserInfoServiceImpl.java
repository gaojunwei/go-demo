package com.gjw.go.service.impl;

import com.gjw.go.common.enums.FromSourceEnums;
import com.gjw.go.persistence.dbone.UserInfoDbOneDao;
import com.gjw.go.persistence.dbtwo.UserInfoDbTwoDao;
import com.gjw.go.persistence.domain.UserInfo;
import com.gjw.go.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoDbOneDao userInfoDbOneDao;
    @Resource
    private UserInfoDbTwoDao userInfoDbTwoDao;

    @Override
    public UserInfo getById(FromSourceEnums fromSourceEnums, Long userId) {
        if(Objects.equals(fromSourceEnums,FromSourceEnums.B)){
            return userInfoDbTwoDao.selectById(userId);
        }
        return userInfoDbOneDao.selectById(userId);
    }
}
