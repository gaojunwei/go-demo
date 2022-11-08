package com.gjw.common.innovation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/client/{uniqueCode}")
public class WebsocketController {

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static final ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<>();
    private final String uniqueCodeKey = "uniqueCode";

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uniqueCode") String uniqueCode) {
        //注册链接失败
        if (webSocketSet.containsKey(uniqueCode)) {
            session.getAsyncRemote().sendText("invalid");
            log.warn("[WebSocket] 链接已被注册 响应 invalid");
            return;
        }
        //设备编码与当前链接绑定
        session.getUserProperties().put(uniqueCodeKey, uniqueCode);
        //添加到链接管理池
        webSocketSet.put(uniqueCode, session);
        //响应注册成功标识
        session.getAsyncRemote().sendText("valid");
    }

    /**
     * 接收到消息
     */
    @OnMessage
    public void onMsg(Session session, String message) {
        log.info("收到消息 clientId:{},message{}", getClientId(session), message);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        log.info("连接关闭 {}", getClientId(session));
    }

    private String getClientId(Session session) {
        return (String) session.getUserProperties().get(uniqueCodeKey);
    }
}