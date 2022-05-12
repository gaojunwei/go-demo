package com.jdjr.crawler.tcpj.service.mq.kafka;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaService {
    @Resource
    private Gson gson;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    //配置监听
    @PostConstruct
    private void listener() {
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                logger.info("异步处理-MQ消息发送成功 {}", gson.toJson(producerRecord));
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, Exception e) {
                logger.error("异步处理-MQ消息发送失败 {},errorMsg:{}", gson.toJson(producerRecord), e.getMessage(), e);
            }
        });
    }

    /**
     * 发送MQ消息
     */
    public void send(String msg) {
        logger.info("发送的MQ消息 {}", msg);
        kafkaTemplate.send("gjwtest", null, msg);
    }

    /*//不指定group，默认取yml里配置的
    @KafkaListener(id = "kafka-consumer-one",topics = {"gjwtest"}, groupId = "group-one")
    public void onMessage(ConsumerRecord<?, ?> record) {
        logger.info("收到消息,topic:{}," +
                "偏移量:{}," +
                "消息体:{}," +
                "分区{}," +
                "headers:{}",record.topic(),record.offset(),record.value(),record.partition(),record.headers());
    }

    //不指定group，默认取yml里配置的
    @KafkaListener(id = "kafka-consumer-999",topics = {"gjwtest"}, groupId = "group-777",idIsGroup = false, concurrency="2")
    public void onMessage1(ConsumerRecord<?, ?> record) {
        logger.info("收到消息,topic:{}," +
                "偏移量:{}," +
                "消息体:{}," +
                "分区{}," +
                "headers:{}",record.topic(),record.offset(),record.value(),record.partition(),record.headers());
    }*/

    /*//不指定group，默认取yml里配置的
    @KafkaListener(id = "kafka-consumer-999",topics = {"gjwtest"}, groupId = "group-777",idIsGroup = false)
    public void onMessage1(List<ConsumerRecord> record, Acknowledgment acknowledgment) {
        logger.info("消费消息数量：{}",record.size());
        for (int i = 0; i < record.size(); i++) {
            ConsumerRecord record1 = record.get(i);
            System.out.println(record1.value());
        }
        try {
            Thread.sleep(200);
            System.out.println("消费完成");
            acknowledgment.acknowledge();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    //不指定group，默认取yml里配置的
    /*@KafkaListener(id = "kafka-consumer-999", topics = {"gjwtest"}, groupId = "group-777", idIsGroup = false, clientIdPrefix = "appName")
    public void onMessage1(ConsumerRecord record, Acknowledgment acknowledgment) {
        logger.info("收到消息：{}", record.value());
        try {
            Thread.sleep(200);
            System.out.println("消费完成");
            acknowledgment.acknowledge();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    //不指定group，默认取yml里配置的
    @KafkaListener(id = "kafka-consumer-99X", topics = {"gjwtest"}, groupId = "group-777", idIsGroup = false, clientIdPrefix = "appName2",concurrency = "2")
    public void onMessage2(ConsumerRecord record, Acknowledgment acknowledgment) {
        logger.info("收到消息SSS：{}", record.value());
        try {
            Thread.sleep(200);
            System.out.println("消费完成");
            acknowledgment.acknowledge();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
