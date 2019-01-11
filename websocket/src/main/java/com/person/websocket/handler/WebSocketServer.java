package com.person.websocket.handler;

import com.person.websocket.model.Charter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 一对一socketServer
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
@ServerEndpoint(value = "/websocket/{charter}/{target}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    private static Map<String, Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(@PathParam("charter") String charter, Session session) {
        sessionMap.put(charter, session);
        log.info("{}老吊上线了！", charter);
    }

    @OnMessage
    public void onMessage(@PathParam("charter") String charter, @PathParam("target") String target, String message, Session session) {
        log.info("来自客户端{}的消息:{}", charter, message);
        Session targetSession = sessionMap.get(target);
        try {
            if (targetSession == null) {
                sendMessage(session, "系统提示: 对方已离线");
            } else {
                sendMessage(targetSession, charter + ":" + message);
            }
        } catch (IOException e) {
            log.error("发送消息异常", e);
        }
    }

    @OnClose
    public void OnClose(@PathParam("charter") String charter) {
        sessionMap.remove(charter);
        log.info("有连接断开！");
    }

    public void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
