package com.cm.WebSocketServer;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 31373
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/chat/{userId}")
public class ChatMessageWebSocketServer {

    //存储用户id与WebSocket连接的映射关系
    private static ConcurrentHashMap<Long, ChatMessageWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    @Getter
    private Session session;

    private Long userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);
        log.info("用户{}建立聊天连接，当前在线人数：{}", userId, webSocketMap.size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(userId);
        log.info("用户{}断开聊天连接，当前在线人数：{}", userId, webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户{}的消息: {}", userId, message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户{}聊天连接发生错误", userId, error);
        webSocketMap.remove(userId);
    }

    /**
     * 给指定用户发送消息
     */
    public static void sendMessageToUser(Long userId, String message) {
        ChatMessageWebSocketServer webSocket = webSocketMap.get(userId);
        if (webSocket != null && webSocket.getSession().isOpen()) {
            try {
                webSocket.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("发送消息给用户{}失败", userId, e);
            }
        } else {
            log.warn("用户{}不在线或连接已关闭，无法发送消息", userId);
        }
    }

    /**
     * 批量发送消息
     */
    public static void broadcastMessage(List<Long> userIds, String message) {
        for (Long userId : userIds) {
            sendMessageToUser(userId, message);
        }
    }

    /**
     * 获取在线用户列表
     */
    public static List<Long> getOnlineUsers() {
        return new ArrayList<>(webSocketMap.keySet());
    }
}
