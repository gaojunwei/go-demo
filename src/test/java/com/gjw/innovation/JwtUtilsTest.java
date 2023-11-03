package com.gjw.innovation;

import com.alibaba.fastjson2.JSON;
import com.gjw.common.result.SingleResult;
import com.gjw.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

public class JwtUtilsTest {

    @Test
    public void test001(){
        String str = JwtUtils.createJWT("119","user1",3600L);
        System.out.println(str);
        SingleResult<Claims> str1 = JwtUtils.validateJWT(str);

        System.out.println(str1.getData().getSubject());
        System.out.println(str1.getData().getId());
        System.out.println(JSON.toJSONString(str1));

    }
}
