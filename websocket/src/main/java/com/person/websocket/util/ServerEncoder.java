package com.person.websocket.util;

import com.alibaba.fastjson.JSON;
import com.person.websocket.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * 类说明
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-14
 */
public class ServerEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return JSON.toJSONString(message);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
