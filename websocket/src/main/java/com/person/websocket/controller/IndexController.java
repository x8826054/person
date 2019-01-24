package com.person.websocket.controller;

import com.person.websocket.enums.GroupChatEnum;
import com.person.websocket.model.Chatter;
import com.person.websocket.util.OnlinePool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 类说明
 * <p>
 *
 * @author 徐擂
 * @version 1.0.0
 * @date 2019-1-11
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String inputMessage() {
        return "inputMessage";
    }

    @RequestMapping("/go")
    public String go(Chatter chatter, Model model) {
        chatter.setId(UUID.randomUUID().toString().replace("-", ""));
        OnlinePool.onlineChatter.put(chatter.getId(), chatter);
        model.addAttribute("name", chatter.getNickName());
        model.addAttribute("id", chatter.getId());
        model.addAttribute("all", GroupChatEnum.ALL_GROUP.getCode());
        return "index";
    }

    @ResponseBody
    @RequestMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.putAll(OnlinePool.onlineChatter);
        return dataMap;
    }



}
