package com.test.mvc;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.github.jsonzou.jmockdata.JMockData;
import com.gjw.common.innovation.TApplication;
import com.gjw.common.innovation.domain.UserInfo;
import com.gjw.common.innovation.service.UserService;
import com.test.config.TestMtgConfig;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = TApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpringTest {

    //@MockBean
    @SpyBean
    private UserService userService;

    @Autowired
    private TestMtgConfig testMtgConfig;

    @Test
    @DisplayName("根据ID获取用户信息")
    @Order(0)
    public void getUserById() {
        System.out.println(">>>"+testMtgConfig.getAppName());

        UserInfo userInfo = JMockData.mock(UserInfo.class);
        userInfo.setId(1L);
        Mockito.doReturn(userInfo).when(userService).getById(Mockito.any());
        UserInfo data = userService.getById(Mockito.anyLong());
        Assertions.assertEquals(1L, data.getId());
    }

    @Test
    @DisplayName("查询用户集合")
    @Order(1)
    public void listUserByCon() {
        List<UserInfo> users = new ArrayList<>();
        users.add(JMockData.mock(UserInfo.class));
        users.add(JMockData.mock(UserInfo.class));

        Mockito.doReturn(users).when(userService).listByCom(Mockito.any(),Mockito.any());

        List<UserInfo> list = userService.listByCom(Mockito.anyLong(), Mockito.anyString());
        list.stream().forEach(System.out::println);
        Assertions.assertTrue(CollectionUtil.isNotEmpty(list));
    }
}
