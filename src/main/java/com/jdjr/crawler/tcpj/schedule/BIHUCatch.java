package com.jdjr.crawler.tcpj.schedule;

import com.alibaba.fastjson.JSON;
import com.jdjr.crawler.tcpj.schedule.data.BaseData;
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
public class BIHUCatch {
    /**
     * 同城票据多账号登录态缓存池
     */
    private static Map<String, BaseData> dataMap = new ConcurrentHashMap<>();
    /**
     * 登录态的有效时长（单位:毫秒）12小时
     */
    private static int YXQ = 12 * 3600 * 1000;
    private static int logCount = 10;
    /**
     * 记录最近100个定时任务的执行情况
     */
    private static ArrayDeque<String> logQueue = new ArrayDeque<>(logCount);

    /**
     * 同城票据多账号登录态改造
     */
    public static void applyValue(String logId, String phone, String token) {
        applyValue(logId, phone, token, null, null);
    }

    /**
     * 同城票据多账号登录态改造
     */
    public static void applyValue(String logId, String phone, String token, Integer isUsed, Date creatTime) {
        synchronized (BIHUCatch.class) {
            BaseData bihuDataOld = dataMap.get(phone);
            if (bihuDataOld == null) {
                BaseData bihuData = new BaseData();
                bihuData.setPhone(phone);
                bihuData.setToken(token);

                if (isUsed == null)
                    bihuData.setIsUsed(0);
                else
                    bihuData.setIsUsed(isUsed);

                if (creatTime == null)
                    bihuData.setCreatTime(new Date());
                else
                    bihuData.setCreatTime(creatTime);
                dataMap.put(phone, bihuData);
            } else {
                bihuDataOld.setToken(token);
                if (creatTime == null)
                    bihuDataOld.setCreatTime(new Date());
                else
                    bihuDataOld.setCreatTime(creatTime);
                logger.info("logId {} after applyValue:{}", logId, JSON.toJSONString(dataMap.get(phone)));
            }
        }
    }

    /**
     * 获取账号Token
     */
    public static BaseData getToken() {
        synchronized (BIHUCatch.class) {
            if (dataMap.isEmpty()) {
                return null;
            }
            List<BaseData> biHuDataList = dataMap.values().stream().collect(Collectors.toList());

            for (BaseData baseData : biHuDataList) {
                //优先使用未使用过的账户Token
                if (baseData.getIsUsed() == 0) {
                    baseData.setIsUsed(1);
                    return baseData;
                }
            }
            //若果已全部使用过，则全部置为未使用状态0,并取第一个Token进行返回
            biHuDataList.stream().forEach(item -> item.setIsUsed(0));
            BaseData result = biHuDataList.get(0);
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
        synchronized (BIHUCatch.class) {
            for (String phone : dataMap.keySet()) {
                BaseData baseData = dataMap.get(phone);
                if ((System.currentTimeMillis() - baseData.getCreatTime().getTime()) > YXQ) {
                    logger.info("bihu phone:{} was expired", phone);
                    dataMap.remove(phone);
                    logger.info("bihu phone:{} was removed success!!!", phone);
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
     * 获取缓存数据
     */
    public static List<BaseData> getCatch() {
        return dataMap.values().stream().sorted(Comparator.comparing(BaseData::getCreatTime)).collect(Collectors.toList());
    }
}
