package com.jdjr.crawler.tcpj.common.util;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 17:23
 **/
public class DateUtils {

    /**
     * 判断当前时间是否在给定的小时点内
     * @param hours
     * @return
     */
    public static boolean isNowInHour(List<Integer> hours){
        LocalDateTime currentTime = LocalDateTime.now();
        Integer hour = currentTime.getHour();
        return hours.contains(hour);
    }
}
