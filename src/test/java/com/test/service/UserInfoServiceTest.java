package com.test.service;

import com.alibaba.fastjson2.JSON;
import com.gjw.go.common.enums.FromSourceEnums;
import com.gjw.go.persistence.domain.UserInfo;
import com.gjw.go.service.UserInfoService;
import com.test.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@Slf4j
public class UserInfoServiceTest extends AbstractTest {

    @Resource
    private UserInfoService userInfoService;

    @Test
    public void getById(){
        //初始化测试数据
        h2Flusher.exeSql("DB/test_data/user_info.sql");

        Long userId = 100L;
        UserInfo userInfo = userInfoService.getById(FromSourceEnums.A,userId);
        log.info("FromSourceEnums:{} 查询结果:{}", FromSourceEnums.A.getValue(),JSON.toJSONString(userInfo));
        Assertions.assertNotNull(userInfo);

        UserInfo userInfo2 = userInfoService.getById(FromSourceEnums.B,userId);
        log.info("FromSourceEnums:{} 查询结果:{}", FromSourceEnums.B.getValue(),JSON.toJSONString(userInfo2));
        Assertions.assertNotNull(userInfo2);
    }
}
