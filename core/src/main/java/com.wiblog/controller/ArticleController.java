package com.wiblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;
import com.wiblog.service.IArticleService;
import com.wiblog.utils.PropertiesUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import lombok.extern.java.Log;


/**
 *  控制层
 *
 * @author pwm
 * @date 2019-06-12
 */
@Log
@RestController
@RequestMapping("/post")
@PropertySource(value = "classpath:/config/wiblog.properties", encoding = "utf-8")
public class ArticleController {

    private IArticleService articleService;


    private String picPath;

    private String host;

    @Value("${host-name}")
    public void setHost(String host){
        this.host = host;
    }

    @Value("${pic-path}")
    public void setPicPath(String picPath){
        this.picPath = picPath;
    }

    @Autowired
    public ArticleController(IArticleService articleService){
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public ServerResponse<IPage> articlePageList(@RequestParam Map<String, Object> params){
        Integer pageNum = Integer.valueOf((String) params.get("pageNum"));
        Integer pageSize = Integer.valueOf((String) params.get("pageSize"));
        Page<Article> page = new Page<>(pageNum, pageSize);
        IPage<Article> articleIPage = articleService.page(page,new QueryWrapper<Article>()
                .select("id","title","tags","article_categories","url","article_url","article_summary","likes","hits","comments_counts","create_time"));
        return ServerResponse.success(articleIPage,"查找文章列表成功");
    }

    @GetMapping("/get/{id}")
    public ServerResponse<Article> getArticleById(@PathVariable Integer id){
        Article article = articleService.getById(id);
        return ServerResponse.success(article,"获取文章成功");
    }

    /**
     * 发表文章
     * @param article article
     * @return ServerResponse
     */
    @PostMapping("/push")
    public ServerResponse<Article> updateArticle(Article article){
        Date date = new Date();
        article.setUpdateTime(date);
        article.setCreateTime(date);
        article.setArticleUrl(host+"/post/"+date.getTime());

        Boolean bool = articleService.save(article);
        if (bool) {
            return ServerResponse.success(null, "文章发表成功");
        }
        return ServerResponse.error("文章发表失败",30001);
    }

    @PostMapping("/upload")
    public ServerResponse upload(MultipartFile file,String post){
        if (file.isEmpty()){
            return ServerResponse.error("上传失败，请选择文件",30000);
        }
        String fileName = file.getOriginalFilename();
        String postPath;
        if (StringUtils.isBlank(post)){
            postPath = picPath;
        }else {
            postPath = picPath + post;
        }
        File postFile = new File(postPath);
        if (!postFile.exists()){
            postFile.mkdirs();
        }
        String filePath = postPath+"/"+fileName;
        File dest = new File(filePath);
        try{
            file.transferTo(dest);
            return ServerResponse.success(filePath,"上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.error("上传失败",30001);
    }

}
