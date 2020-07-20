package com.jdjr.crawler.tcpj.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Date;

/**
 * 同城票据TOKEN数据缓存
 *
 * @Author gaojunwei
 * @Date 2020/7/16 21:56
 **/
@Slf4j
public class TCPJCatch {
    private static String token;
    private static Date creatTime;
    //记录最近100个定时任务的执行情况
    private static ArrayDeque<String> logQueue = new ArrayDeque<>(100);


    public static void applyValue(String token, Date creatTime) {
        TCPJCatch.token = token;
        TCPJCatch.creatTime = creatTime;
    }

    public static void clearValue() {
        TCPJCatch.token = null;
        TCPJCatch.creatTime = null;
    }

    /**
     * 检测是否过期，过期清理掉数据
     */
    public static void checkExpire() {
        if (token == null) {
            return;
        }
        int yxq = 5 * 3600 * 1000;//5小时
        if ((System.currentTimeMillis() - creatTime.getTime()) > yxq) {
            clearValue();
        }
    }

    public synchronized static void addLog(String logStr){
        try{
            if(logQueue.size()>=100){
                logQueue.pollFirst();
            }
            logQueue.addLast(logStr);
        }catch (Exception e){
            logger.warn("任务执行日志记录失败");
        }
    }

    public static Object[] getLog(){
        Object[] array = logQueue.toArray();
        return array;
    }

    public static String getToken() {
        return token;
    }

    public static Date getCreatTime() {
        return creatTime;
    }
}
