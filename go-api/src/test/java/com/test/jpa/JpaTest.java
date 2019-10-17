package com.test.jpa;

import com.alibaba.fastjson.JSON;
import com.gjw.common.innovation.TApplication;
import com.gjw.common.innovation.repository.db1.TUserRepository;
import com.gjw.common.innovation.repository.db1.domain.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: gaojunwei
 * @Date: 2019/7/1 17:41
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TApplication.class)
public class JpaTest {

    @Resource
    private TUserRepository tUserRepository;

    @Test
    public void test001(){
        for (int i = 0; i < 10; i++) {
            TUser user = new TUser();
            user.setUserName("name"+i);
            tUserRepository.save(user);
        }
        List<TUser> list = tUserRepository.findAll();
        System.out.println(JSON.toJSONString(list));
    }
}