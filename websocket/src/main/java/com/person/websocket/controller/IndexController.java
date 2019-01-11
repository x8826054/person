package com.person.websocket.controller;

import com.person.websocket.model.Charter;
import com.person.websocket.util.OnlinePool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

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
    public String go(Charter charter, Model model) {
        OnlinePool.onlineCharter.put(charter.getNickName(), charter);
        model.addAttribute("name", charter.getNickName());
        return "index";
    }

    @ResponseBody
    @RequestMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.putAll(OnlinePool.onlineCharter);
        return dataMap;
    }



}
