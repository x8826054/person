package com.person.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.person.websocket.MessageType;
import com.person.websocket.model.Charter;
import com.person.websocket.model.Message;
import com.person.websocket.util.OnlinePool;
import com.person.websocket.util.ServerEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * web端
 * 一对一socketServer
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
@ServerEndpoint(value = "/websocket/{id}", encoders = { ServerEncoder.class })
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    private static Map<String, Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {
        Charter charter = OnlinePool.onlineCharter.get(id);
        if (charter == null) {
            return;
        }
        for (Map.Entry<String, Session> stringSessionEntry : sessionMap.entrySet()) {
            sendMessage(stringSessionEntry.getValue(), new Message(proxyContent(String.format("老吊【%s】上线了", charter.getNickName()),
                    MessageType.ONLINE_REMIND_MESSAGE.getCode()), MessageType.ONLINE_REMIND_MESSAGE.getCode(), charter));
        }
        sessionMap.put(id, session);
    }

    @OnMessage
    public void onMessage(@PathParam("id") String id, String message, Session session) {
        Charter charter = OnlinePool.onlineCharter.get(id);
        if (charter == null) {
            return;
        }
        log.info("来自客户端{}的消息:{}", charter.getNickName(), message);
        Message msg = JSONObject.parseObject(message, Message.class);
        Session targetSession = sessionMap.get(msg.getTarget());
        if (targetSession == null) {
            sendMessage(session, new Message(proxyContent("对方已离线", MessageType.SYSTEM_MESSAGE.getCode()), MessageType.SYSTEM_MESSAGE.getCode()));
        } else {
            sendMessage(targetSession, new Message(charter.getNickName() + ":" + msg.getContent(), MessageType.CHART_MESSAGE.getCode(), charter));
        }
    }

    @OnClose
    public void OnClose(@PathParam("id") String id) {
        Charter charter = OnlinePool.onlineCharter.get(id);
        sessionMap.remove(id);
        OnlinePool.onlineCharter.remove(id);
        for (Map.Entry<String, Session> stringSessionEntry : sessionMap.entrySet()) {
            sendMessage(stringSessionEntry.getValue(), new Message(proxyContent(String.format("老吊【%s】下线了", charter.getNickName()),
                    MessageType.OFF_LINE_REMIND_MESSAGE.getCode()), MessageType.OFF_LINE_REMIND_MESSAGE.getCode(), charter));
        }
        log.info("{}断开连接", id);
    }

    private void sendMessage(Session session, Message message){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            log.error("发送上线消息异常", e);
        }
    }

    private String proxyContent(String content, int code){
        if (code == MessageType.CHART_MESSAGE.getCode()) {
            return content;
        }
        return MessageType.getDescByCode(code) + ": " + content;
    }
}
