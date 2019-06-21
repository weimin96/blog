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

    /**
     * 后台主页框架
     */
    @GetMapping("/admin")
    public String adminIndex(){
        return "admin/index";
    }

    /**
     * 后台主页内容
     */
    @GetMapping("/admin/home")
    public String adminIndexHome(){
        return "admin/home";
    }

    /**
     * 后台文章中心
     */
    @GetMapping("/admin/articleList")
    public String articleList(){
        return "admin/articleList";
    }

    /**
     * 后台文章编辑
     */
    @GetMapping("/admin/articleEdit")
    public String articleEdit(){
        return "admin/articleEdit";
    }

}
