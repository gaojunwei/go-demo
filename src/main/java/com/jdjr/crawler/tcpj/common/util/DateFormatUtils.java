package com.jdjr.crawler.tcpj.common.util;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: gaojunwei
 * @Date: 2019/7/3 10:44
 * @Description: 日期格式化工具类
 */
@Data
public class DateFormatUtils {

    public static String dateFormat(Date date, FormatEnums formatEnums) {
        return dateFormat(date, formatEnums.getValue());
    }

    public static String dateFormat(long dateTime, FormatEnums formatEnums) {
        return dateFormat(new Date(dateTime), formatEnums.getValue());
    }

    private static String dateFormat(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FormatEnums.yyyy_MM_dd_HH_mm_ss.getValue());
        return format.parse(dateStr);
    }

    /**
     * 获取当前日期的格式化数据
     *
     * @return
     */
    public static String getNowDate() {
        return dateFormat(new Date(), FormatEnums.yyyy_MM_dd_HH_mm_ss.getValue());
    }

    /**
     * 获取当前日期的格式化数据
     *
     * @param formatEnums
     * @return
     */
    public static String getNowDate(FormatEnums formatEnums) {
        return dateFormat(new Date(), formatEnums.getValue());
    }

    /**
     * 日期格式枚举类
     */
    public enum FormatEnums {
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss");
        private String value;

        FormatEnums(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}