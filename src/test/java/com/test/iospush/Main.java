package com.test.iospush;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: gaojunwei
 * @Date: 2019/6/26 9:23
 * @Description:
 */
public class Main {
    public static void main(String[] args) throws Exception
    {
        for (int i = 0; i < 1; i++) {
            doWork();
        }
    }
    public static void doWork()
    {
        String deviceToken = "72e5476254a61a07d35a480e66d54570a7e3d2d7027bbeab05f0ee6b87011e06";
        String alert = "电子价签推送";//push的内容
        int badge = 1;//图标小红圈的数值
        String sound = "default";//铃音

        List<String> tokens = new ArrayList<>();
        tokens.add(deviceToken);

        String certificatePath = "D:/var/push_dev.p12";
        String certificatePassword = "654321";//此处注意导出的证书密码不能为空因为空密码会报错

        try{
            PushNotificationPayload payLoad = new PushNotificationPayload();
            payLoad.addAlert(alert); // 消息内容
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
            payLoad.addSound(sound);//铃音

            PushNotificationManager pushManager = new PushNotificationManager();
            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            AppleNotificationServer appleNotificationServer = new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, false);
            appleNotificationServer.setProxy(AppleNotificationServer.DEVELOPMENT_HOST,AppleNotificationServer.DEVELOPMENT_PORT);
            pushManager.initializeConnection(appleNotificationServer);
            List<PushedNotification> notifications = new ArrayList<>();
            // 发送push消息
            Device device = new BasicDevice();
            device.setToken(tokens.get(0));
            PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
            notifications.add(notification);

            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
            int failed = failedNotifications.size();
            int successful = successfulNotifications.size();
            System.out.println(String.format("发送成功数量:%s,发送失败数量:%s",successful,failed));
            pushManager.stopConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}