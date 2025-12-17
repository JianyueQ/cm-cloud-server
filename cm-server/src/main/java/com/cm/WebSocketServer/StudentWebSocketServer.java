package com.cm.WebSocketServer;

import com.alibaba.fastjson.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 31373
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/attendance/{userId}")
public class StudentWebSocketServer {

    // 静态变量，用来记录当前在线连接数
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<StudentWebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 用于记录用户ID与该WebSocket连接的映射关系
    private static ConcurrentHashMap<Long, StudentWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    @Getter
    private Session session;

    //用户id
    private Long userId;

    /**
     * 连接建立成功调用的方法
     * @param session  Session
     * @param userId   用户id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;
        //this代表当前的webSocket对象
        webSocketMap.put(userId, this);
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新用户加入，用户ID：{}，当前在线人数为：{}", userId, getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        webSocketMap.remove(userId);
        subOnlineCount();
        log.info("有一用户退出，用户ID：{}，当前在线人数为：{}", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自用户{}的消息:{}", userId, message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户{}发生错误", userId, error);
    }

    /**
     * 给指定用户发送消息
     * @param userId 用户id
     * @param message 消息
     */
    public static void sendMessageToUser(Long userId, String message){
        StudentWebSocketServer webSocket = webSocketMap.get(userId);
        if (webSocket != null){
            //获取 session,获取发送者,获取消息
            try {
                webSocket.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("发送消息给用户{}失败", userId, e);
            }
        }else {
            log.warn("用户{}不在线，无法发送消息", userId);
        }
    }

    /**
     * 发送JSON格式的考勤通知给指定用户
     *
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     */
    public static void sendAttendanceNotification(Long userId, String title, String content) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("title", title);
        messageMap.put("content", content);
        messageMap.put("time", LocalDateTime.now().toString());
        String message = JSON.toJSONString(messageMap);
        sendMessageToUser(userId, message);
    }

    /**
     * 发送JSON格式的考勤通知给指定用户
     * @param userId 用户ID
     * @param messageMap 通知
     */
    public static void sendAttendanceNotification(Long userId, Map<String,Object> messageMap) {
        String message = JSON.toJSONString(messageMap);
        sendMessageToUser(userId, message);
    }

    /**
     * 减少在线人数
     */
    public static synchronized void subOnlineCount() {
        StudentWebSocketServer.onlineCount--;
    }

    /**
     * 获取当前在线人数
     * @return onlineCount
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 添加在线人数
     */
    public static synchronized void addOnlineCount() {
        StudentWebSocketServer.onlineCount++;
    }

}
