package com.test.jpa;

import com.alibaba.fastjson.JSON;
import com.jd.innovation.TApplication;
import com.jd.innovation.repository.TUserRepository;
import com.jd.innovation.repository.domain.TUser;
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
            user.setName("name"+i);
            tUserRepository.save(user);
        }
        List<TUser> list = tUserRepository.findAll();
        System.out.println(JSON.toJSONString(list));
    }
}