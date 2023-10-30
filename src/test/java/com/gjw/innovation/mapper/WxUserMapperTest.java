package com.gjw.innovation.mapper;

import com.gjw.innovation.AbstractSpringTest;
import com.gjw.innovation.domain.WxUser;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class WxUserMapperTest extends AbstractSpringTest {
    @Resource
    private WxUserMapper wxUserMapper;

    @Test
    public void test001(){
        WxUser wxUser = new WxUser();
        wxUser.setNickName("测试");
        wxUser.setOpenId("123456");

        wxUser.setCreateTime(LocalDateTime.now());
        wxUser.setCreateBy("1");
        wxUser.setUpdateTime(LocalDateTime.now());
        wxUser.setUpdateBy("2");


        wxUserMapper.insert(wxUser);
    }
}
