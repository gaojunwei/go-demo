package com.test.schedule;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2019/6/11 13:53
 * @Description: quartz测试类
 */
public class QuartzDemo {
    public static void main(String[] args) {
        String cronStr = "0 0/1 * ? * *";
        check(cronStr);
        cronTest(cronStr);
    }

    private static boolean check(String cronStr){
        if(CronExpression.isValidExpression(cronStr)){
            System.out.println("校验通过");
            return true;
        }else {
            System.out.println("校验未通过");
            return false;
        }
    }

    private static void cronTest(String cronStr) {
        try {
            CronExpression exp = new CronExpression(cronStr);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date();
            int i = 0;
            // 循环得到接下来n此的触发时间点，供验证
            while (i < 10) {
                d = exp.getNextValidTimeAfter(d);
                System.out.println(df.format(d));
                ++i;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}