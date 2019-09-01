package com.wiblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wiblog.entity.Article;
import com.wiblog.service.IArticleService;
import com.wiblog.utils.Commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author pwm
 * @date 2019/4/12
 */
@Controller
public class WebController {

    @Autowired
    private IArticleService articleService;

    @Resource
    private Commons commons;

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
     * 跳转文章页
     */
    @GetMapping("/post/{url}")
    public String article(HttpServletRequest request, @PathVariable Long url){
        Date date = new Date();
        date.setTime(url);
        System.out.println("url:"+date);
        Article article = articleService.getOne(new QueryWrapper<Article>().eq("create_time",date));
        if (article == null){
            return "404";
        }
        request.setAttribute("article",article);
        request.setAttribute("commons", commons);
        return "article";
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
