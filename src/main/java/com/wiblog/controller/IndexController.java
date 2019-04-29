package com.wiblog.controller;

import com.github.pagehelper.PageInfo;
import com.wiblog.common.ServerResponse;
import com.wiblog.model.Article;
import com.wiblog.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 * @author pwm
 * @date 2019/4/11
 */
@RestController("/")
public class IndexController {

    @Autowired
    ArticleService articleService;

    @PostMapping("/articles")
    public ServerResponse<PageInfo> articlePageList(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize){
        return articleService.findAllArticle(pageNum,pageSize);
    }
}
