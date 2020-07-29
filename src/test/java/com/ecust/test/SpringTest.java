package com.ecust.test;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.Application;
import com.jdjr.crawler.tcpj.common.enums.BusinessEnums;
import com.jdjr.crawler.tcpj.repository.domain.LoginData;
import com.jdjr.crawler.tcpj.repository.domain.UserAccount;
import com.jdjr.crawler.tcpj.service.UserAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/17 14:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringTest {

    @Resource
    private UserAccountService userAccountService;

    @Test
    public void test002() {
        LoginData loginData = new LoginData();
        loginData.setAccount("13693615037");
        loginData.setSite(BusinessEnums.BIHU.getValue());
        loginData.setToken("SAeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7InRyYWRlcl9pZCI6IjE5MDE5OCIsInRva2VuIjoiMTIwZDQwMTgzMDBiNGE4NDhlNTQ4NjRlNDYxNWM0NTAiLCJzaG9wX2lkIjoiMTg4OTAzIiwicm9sZSI6ImFkbWluIiwiZW50ZXJwcmlzZV9wbGF0Zm9ybV9pZCI6IiIsImNvbXBhbnlfaWRzIjpbXX0sImV4cCI6MTU5NTg4ODkyNX0.XgnzADxEFHWE03EYkTORur7NioSB2D7pm7jKyyr_oLk");

        userAccountService.saveToken(loginData);
    }

    @Test
    public void test001() {
        List<UserAccount> accountList = userAccountService.getAll(BusinessEnums.BIHU);
        System.out.println(JSON.toJSONString(accountList));
    }
}
