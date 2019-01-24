package com.person.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.person.websocket.enums.GroupChatEnum;
import com.person.websocket.enums.MessageTypeEnum;
import com.person.websocket.model.Chatter;
import com.person.websocket.model.Message;
import com.person.websocket.util.OnlinePool;
import com.person.websocket.util.ServerEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
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
@ServerEndpoint(value = "/webServer/{id}", encoders = { ServerEncoder.class })
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {
        Chatter chatter = OnlinePool.onlineChatter.get(id);
        if (chatter == null) {
            return;
        }

        for (Map.Entry<String, Session> stringSessionEntry : OnlinePool.sessionMap.entrySet()) {
            if (id.equals(stringSessionEntry.getKey())) {
                continue;
            }
            sendMessage(stringSessionEntry.getValue(), new Message(proxyContent(String.format("老吊【%s】上线了", chatter.getNickName()),
                    MessageTypeEnum.ONLINE_REMIND_MESSAGE.getCode()), MessageTypeEnum.ONLINE_REMIND_MESSAGE.getCode(), chatter));
        }

        // 添加在线session
        OnlinePool.sessionMap.put(id, session);
        // 加入所有群聊
        Map<String, Session> sessionMap = OnlinePool.chatGroupSessionMap.get(Integer.toString(GroupChatEnum.ALL_GROUP.getCode()));
        if (CollectionUtils.isEmpty(sessionMap)) {
            sessionMap = new HashMap<>();
            sessionMap.put(id, session);
            OnlinePool.chatGroupSessionMap.put(Integer.toString(GroupChatEnum.ALL_GROUP.getCode()), sessionMap);
        } else {
            sessionMap.put(id, session);
        }
    }

    @OnClose
    public void onClose(@PathParam("id") String id) {
        Chatter chatter = OnlinePool.onlineChatter.get(id);
        if (chatter != null) {
            for (Map.Entry<String, Session> stringSessionEntry : OnlinePool.sessionMap.entrySet()) {
                if (id.equals(stringSessionEntry.getKey())) {
                    continue;
                }
                sendMessage(stringSessionEntry.getValue(), new Message(proxyContent(String.format("老吊【%s】下线了", chatter.getNickName()),
                        MessageTypeEnum.OFF_LINE_REMIND_MESSAGE.getCode()), MessageTypeEnum.OFF_LINE_REMIND_MESSAGE.getCode(), chatter));
            }
        }
        OnlinePool.sessionMap.remove(id);
        OnlinePool.onlineChatter.remove(id);
        // 退出所有群聊
        Map<String, Session> sessionMap = OnlinePool.chatGroupSessionMap.get(Integer.toString(GroupChatEnum.ALL_GROUP.getCode()));
        if (!CollectionUtils.isEmpty(sessionMap)) {
            sessionMap.remove(id);
        }
        log.info("{}断开连接", id);
    }

    @OnMessage
    public void onMessage(@PathParam("id") String id, String message, Session session) {
        Chatter chatter = OnlinePool.onlineChatter.get(id);
        if (chatter == null) {
            return;
        }
        log.info("来自客户端{}的消息:{}", chatter.getNickName(), message);
        Message msg = JSONObject.parseObject(message, Message.class);
        if (msg.getType() == MessageTypeEnum.CHAT_MESSAGE.getCode()) {
            Session targetSession = OnlinePool.sessionMap.get(msg.getTarget());
            if (targetSession == null) {
                sendMessage(session, new Message(proxyContent("对方已离线", MessageTypeEnum.SYSTEM_MESSAGE.getCode()), MessageTypeEnum.SYSTEM_MESSAGE.getCode()));
            } else {
                sendMessage(targetSession, new Message(chatter.getNickName() + ":" + msg.getContent(), MessageTypeEnum.CHAT_MESSAGE.getCode(), chatter));
            }
        } else if (msg.getType() == MessageTypeEnum.CHAR_GROUP_MESSAGE.getCode()) {
            Map<String, Session> sessionMap = OnlinePool.chatGroupSessionMap.get(msg.getTarget());
            for (Map.Entry<String, Session> sessionEntry : sessionMap.entrySet()) {
                if (id.equals(sessionEntry.getKey())) {
                    continue;
                }
                sendMessage(sessionEntry.getValue(), new Message(chatter.getNickName() + ":" + msg.getContent(), MessageTypeEnum.CHAR_GROUP_MESSAGE.getCode(), chatter));
            }
        }
    }

    private void sendMessage(Session session, Message message){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            log.error("发送上线消息异常", e);
        }
    }

    private String proxyContent(String content, int code){
        if (code == MessageTypeEnum.CHAT_MESSAGE.getCode()) {
            return content;
        }
        return MessageTypeEnum.getDescByCode(code) + ": " + content;
    }

}
