package com.person.websocket.util;

import com.person.websocket.model.Chatter;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来维护在线人员信息
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
public class OnlinePool {

    /**
     * 在线人员池
     * key:人员id value:人员信息
     */
    public static final Map<String, Chatter> onlineChatter = new HashMap<>();

    /**
     * 在线session池
     * key:人员id value:人员session
     */
    public static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 群聊session池
     * key:群聊id value:sessionMap
     */
    public static Map<String, Map<String, Session>> chatGroupSessionMap = new HashMap<>();



}
