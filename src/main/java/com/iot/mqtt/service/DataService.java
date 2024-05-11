package com.iot.mqtt.service;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.iot.mqtt.mapper.DataBaseMapper;
import com.iot.mqtt.producer.IotMqttGateway;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class DataService {

    @Resource
    private DataBaseMapper dataBaseMapper;
    @Resource
    private IotMqttGateway mqttGateway;


    private final String cardNumber = "card_number";
    private final String relatedId = "related_id";
    private final String personnelId = "id";
    private final String takeCardTopic = "/gjw/position/application/takeCard";
    private final String pointTopic = "/position/application/position";
    private final String returnCardTopic = "/gjw/position/application/returnCard";
    //private final String limit = " limit 1";
    private final String limit = " limit 498";
    private final String url = "http://127.0.0.1:8080/cardSender/faceIdentityNotify";

    private String template = "{\"uniqueId\": \"%s\",\"personId\": %s,\"cardId\": %s,\"result\": \"0\",\"remark\": \"\",\"cardSenderId\": 34,\"takeTime\": \"2024-05-10 14:55:40\"}";


    public void initData() {
        //查询卡信息
        List<Map<String, Object>> cardList = dataBaseMapper.list("select * from person_position.card where card_number>=6600001 and card_number <= 6600498 order by card_number asc" + limit);
        //查询人员信息
        List<Map<String, Object>> personnelList = dataBaseMapper.list("select * from person_position.personnel where id<=1782243108535590914 and enabled=1 and del_flag=0 order by id desc" + limit);
        int all = personnelList.size();
        for (int i = 0; i < personnelList.size(); i++) {
            Map<String, Object> person = personnelList.get(i);
            Map<String, Object> card = cardList.get(i);
            //初始化卡和发卡机的关系
            initCardSender(MapUtil.getLong(card, cardNumber));
            //生成代发卡记录
            creatRecord(MapUtil.getLong(person, personnelId));
            //发送取卡成功mqtt消息
            String msg = getTakeCardMqtt(MapUtil.getLong(person, personnelId), MapUtil.getLong(card, cardNumber));
            mqttGateway.sendMessage2Mqtt(takeCardTopic, 1, msg);
            log.info("发送取卡mqtt {}/{} takeCardTopic:{},msg:{}", (i + 1), all, takeCardTopic, msg);
        }
    }

    public void sendLocation() {
        //查询卡信息
        List<Map<String, Object>> cardList = dataBaseMapper.list("select * from person_position.card where card_number>=6600001 and card_number <= 6600498 order by card_number asc" + limit);

        /**
         * 获取人员和卡信息
         */
        Map<Long,Long> map1 = new HashMap<>();
        Map<Long,Long> map2 = new HashMap<>();

        int middle = cardList.size()/2;
        AtomicInteger a = new AtomicInteger();
        cardList.stream().forEach(item->{
            if(a.get() >= middle){
                map1.put(MapUtil.getLong(item,cardNumber),MapUtil.getLong(item,relatedId));
            }else {
                map2.put(MapUtil.getLong(item,cardNumber),MapUtil.getLong(item,relatedId));
            }
            a.getAndIncrement();
        });

       new Thread(()->{
           while (true){
               sendData(map1);
           }
       }).start();

       new Thread(()->{
           while (true){
               try {
                   TimeUnit.SECONDS.sleep(5);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               sendData(map2);
           }
       }).start();



    }


    String position="{\"uniqueId\":\"%s\",\"cardId\":%s,\"layerId\":\"矿石作业区1\",\"layerHeight\":0,\"latitude\":%s,\"longitude\":%s,\"cardPower\":100,\"acceptTime\":\"%s\",\"cardStatus\":\"1\",\"stillStatus\":0,\"pressure\":null,\"personId\":\"%s\",\"beaconId\":204808,\"deptName\":null,\"realName\":\"双双\",\"jobNumber\":null,\"personType\":\"staff\"}";
    private void sendData(Map<Long,Long> data){
        for (Map.Entry<Long,Long> entry : data.entrySet()){
            Long cardNo = entry.getKey();
            Long userId = entry.getValue();
            Pair<BigDecimal,BigDecimal> point = getPoint();
            String acceptTime = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
            String mqttStr = String.format(position,IdUtil.fastSimpleUUID(),cardNo,point.getKey(),point.getValue(),acceptTime,userId);
            System.out.println("发送MQTT消息："+mqttStr);
            mqttGateway.sendMessage2Mqtt(pointTopic, 1, mqttStr);
        }
    }
    Random random = new Random();

    /**
     * 随机生成定位数据
     */
    private Pair<BigDecimal,BigDecimal> getPoint(){
        BigDecimal maxlong = new BigDecimal("117.92496155247434");
        BigDecimal minlong = new BigDecimal("117.90225024696431");
        BigDecimal maxlat = new BigDecimal("38.37231170059752");
        BigDecimal minlat = new BigDecimal("38.35674425969578");

        BigDecimal newLong = new BigDecimal(Double.toString(random.nextDouble(minlong.doubleValue(),maxlong.doubleValue())));
        BigDecimal newLat = new BigDecimal(Double.toString(random.nextDouble(minlat.doubleValue(),maxlat.doubleValue())));

        return new Pair<>(newLat,newLong);
    }

    private void initCardSender(Long cardNumber) {
        if (dataBaseMapper.exist("XR20230912", cardNumber) == 0) {
            dataBaseMapper.add("XR20230912", cardNumber);
        }
    }

    private String getTakeCardMqtt(Long personId, Long cardId) {
        return String.format(template, IdUtil.simpleUUID(), personId, cardId);
    }

    private void creatRecord(Long personnelId) {
        JSONObject param = JSONUtil.createObj();
        param.put("personnelId", personnelId);
        param.put("cardSenderId", 34);
        String result = HttpUtil.createPost(url).contentType("application/json").body(param.toString()).execute().body();
        /*if (MapUtil.getInt(JSON.parseObject(result), "code").intValue() != 200) {
            throw new RuntimeException("生成代发卡记录失败 " + MapUtil.getStr(JSON.parseObject(result), "msg"));
        }*/
    }


    String returnCard = "{\"uniqueId\":\"%s\",\"personId\":%s,\"cardId\":%s,\"result\":\"0\",\"cardSenderId\":34,\"returnTime\":\"%s\",\"remark\":\"轮 询 还 卡 \"}";
    public void returnCard(){
        //查询卡信息
        List<Map<String, Object>> cardList = dataBaseMapper.list("select * from person_position.card where card_number>=6600001 and card_number <= 6600498 order by card_number asc" + limit);

        /**
         * 获取人员和卡信息
         */
        cardList.stream().forEach(item->{
            String mqttStr = String.format(returnCard,IdUtil.simpleUUID(),MapUtil.getLong(item,relatedId),MapUtil.getLong(item,cardNumber),DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            System.out.println("发送MQTT消息："+mqttStr);
            mqttGateway.sendMessage2Mqtt(returnCardTopic, 1, mqttStr);
        });
    }

}
