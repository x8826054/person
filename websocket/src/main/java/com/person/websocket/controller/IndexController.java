package com.person.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping({"/", "/index"})
    public String register(String name, String target, Model model) {
        model.addAttribute("name", StringUtils.isEmpty(name) ? "李四" : name);
        model.addAttribute("target", StringUtils.isEmpty(name) ? "张三" : target);
        return "index";
    }
}
