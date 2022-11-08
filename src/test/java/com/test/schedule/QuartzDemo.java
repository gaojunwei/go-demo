package com.test.schedule;

import com.alibaba.fastjson.JSON;
import org.quartz.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: gaojunwei
 * @Date: 2019/6/11 13:53
 * @Description: quartz测试类
 */
public class QuartzDemo {
    public static void main(String[] args) throws ParseException {
        String cronStr = "0 0/1 * ? * *";
        //check(cronStr);
        //cronTest(cronStr);

        System.out.println(JSON.toJSONString(seeExcuteTime(cronStr)));
    }

    private static boolean check(String cronStr) {
        if (CronExpression.isValidExpression(cronStr)) {
            System.out.println("校验通过");
            return true;
        } else {
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

    /**
     * @param cron
     * @return
     * @desc 计算表达式近20次时间
     * @auth josnow
     * @date 2017年5月31日 下午12:16:25
     */
    public static List<String> seeExcuteTime(String cron) throws ParseException, IllegalArgumentException {
        if (StringUtils.isEmpty(cron)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<String> list = new ArrayList<>(20);

        Date nextTimePoint = new Date();
        for (int i = 0; i < 20; i++) {
            // 计算下次时间点的开始时间
            nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
            list.add(sdf.format(nextTimePoint));
        }
        return list;
    }
}