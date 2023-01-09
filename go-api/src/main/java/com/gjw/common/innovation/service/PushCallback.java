package com.gjw.common.innovation.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class PushCallback implements MqttCallbackExtended {

    @Override
    public void connectionLost(Throwable cause) {
        /** 连接丢失后，一般在这里面进行重连 **/
        if (MqttPushClient.getClient() != null) {
            while (true) {
                try {
                    logger.info("==============》》》[MQTT] 连接断开，5S之后尝试重连...");
                    Thread.sleep(5000);
                    if (MqttPushClient.reConnect()) {
                        logger.info("=============>>重连成功");
                    }
                    break;
                } catch (Exception e) {
                    logger.error("=============>>>[MQTT] 连接断开，重连失败！<<=============");
                }
            }
        }
        logger.info(cause.getMessage());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        logger.info("publish后会执行到这里");
        logger.info("pushComplete==============>>>" + token.isComplete());
    }


    /**
     * 监听对应的主题消息
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        String Payload = new String(message.getPayload());
        logger.info("============》》接收消息主题 : " + topic);
        logger.info("============》》接收消息Qos : " + message.getQos());
        logger.info("============》》接收消息内容 : " + Payload);
        logger.info("============》》接收ID : " + message.getId());
        logger.info("接收数据结束 下面可以执行数据处理操作");
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        logger.info("connectComplete reconnect:{},serverURI:{}", reconnect, serverURI);
    }
}