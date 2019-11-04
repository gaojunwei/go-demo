package com.test.db;

import com.alibaba.fastjson.JSON;
import com.gjw.common.innovation.TApplication;
import com.gjw.common.innovation.repository.TUserDao;
import com.gjw.common.innovation.repository.domain.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author gaojunwei
 * @date 2019/11/4 14:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TApplication.class)
public class MysqlTest {

    @Resource
    private TUserDao tUserDao;

    @Test
    public void test001(){
        TUser user = new TUser();
        user.setUserName("gjw");
        user.setUserPwd("123456");

        int counts = tUserDao.insert(user);
        System.out.println(counts);
        System.out.println(JSON.toJSONString(user));
    }
}