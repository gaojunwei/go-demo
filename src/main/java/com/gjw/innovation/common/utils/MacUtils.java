package com.gjw.innovation.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: MAC地址工具类
 */
public class MacUtils {
    /**
     * 校验是否为MAC地址
     *
     * @param mac mac地址
     * @return true:是Mac地址格式，false：不是Mac地址格式
     */
    public static boolean isMac(String mac) {
        Pattern p = Pattern.compile("^([0-9a-fA-F]{2})(([/\\s:-][0-9a-fA-F]{2}){5})$");
        Pattern p2 = Pattern.compile("^([0-9a-fA-F]{2})(([0-9a-fA-F]{2}){5})$");

        Matcher m = p.matcher(mac);
        Matcher m2 = p2.matcher(mac);
        if (m.matches() || m2.matches())
            return true;
        return false;
    }


    /**
     * 格式化MAC地址，默认格式（80EACA00000F）
     * @param mac
     * @return
     */
    public static String formateMac(String mac){
        return formateMac(mac,null);
    }

    /**
     * 格式MAC地址
     * @param mac
     * @param splitStr 格式化的分隔符
     * @return
     */
    public static String formateMac(String mac,String splitStr){
        if(StringUtils.isEmpty(mac))
            return null;
        String macStr = mac.toUpperCase().replaceAll("-","").replaceAll(":","");
        if(StringUtils.isEmpty(splitStr))
            return null;
        String formateMac = null;
        if(isMac(macStr)){
            formateMac = macStr.substring(0,2).concat(splitStr)
                    .concat(macStr.substring(2,4)).concat(splitStr)
                    .concat(macStr.substring(4,6)).concat(splitStr)
                    .concat(macStr.substring(6,8)).concat(splitStr)
                    .concat(macStr.substring(8,10)).concat(splitStr)
                    .concat(macStr.substring(10,12));
        }
        return formateMac;
    }

}
