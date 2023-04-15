package com.gjw.common.innovation.service.impl;

import com.github.jsonzou.jmockdata.JMockData;
import com.gjw.common.innovation.domain.UserInfo;
import com.gjw.common.innovation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getById(Long id) {
        logger.info("查询用户信息 id:{}", id);
        return JMockData.mock(UserInfo.class);
    }

    @Override
    public List<UserInfo> listByCom(Long id, String name) {
        logger.info("查询用户列表 id:{},name:{}", id, name);
        return Arrays.asList(JMockData.mock(UserInfo.class), JMockData.mock(UserInfo.class), JMockData.mock(UserInfo.class));
    }
}
