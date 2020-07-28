package com.jdjr.crawler.tcpj.schedule;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.schedule.data.TcpjData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 同城票据TOKEN数据缓存
 *
 * @Author gaojunwei
 * @Date 2020/7/16 21:56
 **/
@Slf4j
public class TCPJCatch {
    /**
     * 同城票据多账号登录态缓存池
     */
    private static Map<String, TcpjData> dataMap = new ConcurrentHashMap<>();
    /**
     * 登录态的有效时长（单位:毫秒）5小时
     */
    private static int YXQ = 5 * 3600 * 1000;
    private static int logCount = 20;
    /**
     * 记录最近100个定时任务的执行情况
     */
    private static ArrayDeque<String> logQueue = new ArrayDeque<>(logCount);

    /**
     * 同城票据多账号登录态改造
     */
    public static void applyValue(String logId, String phone, Integer phoneType, String token) {
        applyValue(logId, phone, phoneType, token, null,null);
    }

    /**
     * 同城票据多账号登录态改造
     */
    public static void applyValue(String logId, String phone, Integer phoneType, String token,Integer isUsed, Date creatTime) {
        synchronized (TCPJCatch.class) {
            TcpjData tcpjDataOld = dataMap.get(phone);
            if (tcpjDataOld == null) {
                TcpjData tcpjData = new TcpjData();
                tcpjData.setPhone(phone);
                tcpjData.setPhoneType(phoneType);
                tcpjData.setToken(token);
                tcpjData.setIsUsed(0);
                if (isUsed == null)
                    tcpjData.setIsUsed(0);
                else
                    tcpjData.setIsUsed(isUsed);

                if (creatTime == null)
                    tcpjData.setCreatTime(new Date());
                else
                    tcpjData.setCreatTime(creatTime);
                dataMap.put(phone, tcpjData);
                logger.info("logId {} add applyValue:{}", logId, JSON.toJSONString(dataMap.get(phone)));
            } else {
                logger.info("logId {} before applyValue:{}", logId, JSON.toJSONString(dataMap.get(phone)));
                tcpjDataOld.setToken(token);
                if (creatTime == null)
                    tcpjDataOld.setCreatTime(new Date());
                else
                    tcpjDataOld.setCreatTime(creatTime);
                logger.info("logId {} after applyValue:{}", logId, JSON.toJSONString(dataMap.get(phone)));
            }
        }
    }

    /**
     * 获取普通账户Token
     */
    public static TcpjData getTokenType0() {
        return getToken(0);
    }

    /**
     * 获取爬取票面账户Token
     */
    public static TcpjData getTokenType1() {
        return getToken(1);
    }

    /**
     * 获取爬取指定类型的账号Token
     */
    private static TcpjData getToken(Integer phoneType) {
        synchronized (TCPJCatch.class) {
            if (dataMap.isEmpty()) {
                return null;
            }
            //过滤出指定类型账户
            List<TcpjData> tcpjDataList = dataMap.values().stream().filter(item -> item.getPhoneType().intValue() == phoneType.intValue()).collect(Collectors.toList());
            if (tcpjDataList.isEmpty()) {
                return null;
            }
            for (TcpjData tcpjData : tcpjDataList) {
                //优先使用未使用过的账户Token
                if (tcpjData.getIsUsed() == 0) {
                    tcpjData.setIsUsed(1);
                    return tcpjData;
                }
            }
            //若果已全部使用过，则全部置为未使用状态0,并取第一个Token进行返回
            tcpjDataList.stream().forEach(item -> item.setIsUsed(0));
            TcpjData result = tcpjDataList.get(0);
            result.setIsUsed(1);
            return result;
        }
    }


    /**
     * 检测是否过期，过期清理掉数据
     */
    public static void checkExpire() {
        if (dataMap.size() == 0) {
            return;
        }
        synchronized (TCPJCatch.class) {
            for (String phone : dataMap.keySet()) {
                TcpjData tcpjData = dataMap.get(phone);
                if ((System.currentTimeMillis() - tcpjData.getCreatTime().getTime()) > YXQ) {
                    logger.info("tcpj phone:{} was expired", phone);
                    dataMap.remove(phone);
                    logger.info("tcpj phone:{} was removed success!!!", phone);
                }
            }
        }
    }

    /**
     * 添加日志记录
     */
    public synchronized static void addLog(String logStr) {
        try {
            if (logQueue.size() >= logCount) {
                logQueue.pollFirst();
            }
            logQueue.addLast(logStr);
        } catch (Exception e) {
            logger.warn("任务执行日志记录失败");
        }
    }

    /**
     * 获取日志记录
     */
    public static Object[] getLog() {
        Object[] array = logQueue.toArray();
        return array;
    }

    /**
     * 获取日志记录
     */
    public static List<TcpjData> getCatch() {
        return dataMap.values().stream().sorted(Comparator.comparing(TcpjData::getPhoneType).thenComparing(TcpjData::getCreatTime)).collect(Collectors.toList());
    }
}
