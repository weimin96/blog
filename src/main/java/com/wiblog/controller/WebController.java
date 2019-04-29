package com.wiblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author pwm
 * @date 2019/4/12
 */
@Controller
public class WebController {

    /**
     * 跳转首页
     */
    @GetMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 跳转登录页
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 跳转注册页
     */
    @GetMapping("/register")
    public String register(){
        return "register";
    }
}
