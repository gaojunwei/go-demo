package com.test.validator;

import com.alibaba.fastjson.JSON;
import com.gjw.common.utils.ValidationUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2018/7/25 15:36
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        String s  = "打孔18";
        System.out.println(s.substring(2));


        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long seconds = ChronoUnit.MILLIS.between(LocalDateTime.now(),midnight);
        System.out.println("当天剩余秒数：" + seconds);
    }

    public static void test002(Param param) {
        // 参数验证
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(param);
        System.out.println(JSON.toJSONString(validResult));
    }
}