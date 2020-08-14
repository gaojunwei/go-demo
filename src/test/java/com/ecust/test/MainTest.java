package com.ecust.test;

import com.jdjr.crawler.tcpj.common.util.DateUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 17:20
 **/
public class MainTest {
    @Test
    public void test001(){
        // 获取当前的日期时间
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();
        System.out.println(hour);

        boolean f = DateUtils.isNowInHour(Arrays.asList(16,18));
        System.out.println(f);
    }
}
