package com.ecust.test;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.common.util.DateUtils;
import com.jdjr.crawler.tcpj.common.util.LocalDateUtils;
import com.jdjr.crawler.tcpj.controller.TestController;
import com.jdjr.crawler.tcpj.schedule.BiHuScheduleTask;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 17:20
 **/
public class MainTest {
    public static void main(String[] args) throws IOException {
        String path = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "rule_desc_config.json")
                .getAbsoluteFile().getPath();
        System.out.println(path);
        List<String> list = FileUtils.readLines(new File(path), "utf-8");
        System.out.println(JSON.toJSONString(list));
    }

    public static void test004() throws IOException {
        InputStream inputStream = BiHuScheduleTask.class.getResourceAsStream("/rule_desc_config.json");
        System.out.println(inputStream.available());
    }

    @Test
    public void test005() {
        System.out.println(this.getClass().getResource("/").toString());
        System.out.println(TestController.class.getResource("/").toString());
    }

    public static String unicodeToString(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    @Test
    public void test003() {
        LocalDate from = LocalDateUtils.dateToLocalDate(new Date());
        LocalDate to = from.plusDays(10);

        System.out.println(from);
        System.out.println(to);

        long result = ChronoUnit.DAYS.between(LocalDate.now(), to);
        System.out.println(result);    // 10


    }

    @Test
    public void test002() {
        StringBuilder str = new StringBuilder("00010000");
        System.out.println("S->" + str.toString() + "  长度：" + str.length());
        str.setCharAt(3, 's');
        System.out.println("E->" + str.toString() + "  长度：" + str.length());
    }

    @Test
    public void test001() {
        // 获取当前的日期时间
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();
        System.out.println(hour);

        boolean f = DateUtils.isNowInHour(Arrays.asList(16, 18));
        System.out.println(f);
    }
}
