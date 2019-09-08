package com.wiblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.aop.RequestRequire;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;
import com.wiblog.service.IArticleService;
import com.wiblog.utils.PinYinUtil;
import com.wiblog.utils.WordFilterUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.java.Log;


/**
 * 控制层
 *
 * @author pwm
 * @date 2019-06-12
 */
@Log
@RestController
@RequestMapping("/post")
@Api(tags = "文章中心api")
@PropertySource(value = "classpath:/config/wiblog.properties", encoding = "utf-8")
public class ArticleController {

    private IArticleService articleService;


    private String picPath;

    private String host;

    @Value("${host-name}")
    public void setHost(String host) {
        this.host = host;
    }

    @Value("${pic-path}")
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Autowired
    private WordFilterUtil wordFilterUtil;

    @Autowired
    private PinYinUtil pinYinUtil;

    @Autowired
    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    @ApiOperation(value="文章列表", notes="获取文章分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认0）",paramType="form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量(默认10)",paramType="form")
    })
    public ServerResponse<IPage> articlePageList(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Article> page = new Page<>(pageNum, pageSize);
        IPage<Article> articleIPage = articleService.page(page, new QueryWrapper<Article>()
                .select("id", "title", "tags", "article_categories", "url", "article_url", "article_summary", "likes", "hits", "comments_counts", "create_time"));
        return ServerResponse.success(articleIPage, "查找文章列表成功");
    }

    @ApiOperation(value="通过url的文章id获取文章详细内容")
    @GetMapping("/get/{id}")
    public ServerResponse<Article> getArticleById(@PathVariable Integer id) {
        Article article = articleService.getById(id);
        return ServerResponse.success(article, "获取文章成功");
    }

    /**
     * 发表文章
     *
     * @param article article
     * @return ServerResponse
     */
    @PostMapping("/push")
    @ApiOperation(value="发表文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题",required = true,paramType="form"),
            @ApiImplicitParam(name = "content", value = "内容",required = true,paramType="form"),
            @ApiImplicitParam(name = "tags", value = "标签",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleCategories", value = "分类",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介",required = true,paramType="form")
    })
    @RequestRequire(require = "title,content,tags,articleCategories,articleSummary", parameter = Article.class)
    public ServerResponse<String> pushArticle(Article article) {
        Date date = new Date();
        article.setUpdateTime(date);
        article.setCreateTime(date);
        // 分词
        List<String> titles = wordFilterUtil.getParticiple(article.getTitle());
        String title = pinYinUtil.getStringPinYin(titles);
        String articleUrl = "/post/" + title;
        Article sameUrlArticle = articleService.getOne(new QueryWrapper<Article>().eq("article_url", articleUrl));
        if (sameUrlArticle != null) {
            return ServerResponse.error("文章发表失败，已存在相同标题", 30001);
        }

        article.setArticleUrl(articleUrl);
        article.setCommentsCounts(0);
        article.setHits(0);
        article.setLikes(0);
        // TODO
        article.setAuthor("areo");

        Boolean bool = articleService.save(article);

        if (bool) {
            return ServerResponse.success(articleUrl, "文章发表成功");
        }
        return ServerResponse.error("文章发表失败", 30001);
    }

    /**
     * 修改文章
     *
     * @param article article
     * @return ServerResponse
     */
    @PostMapping("/update")
    @ApiOperation(value="修改文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题",required = true,paramType="form"),
            @ApiImplicitParam(name = "id", value = "文章id",required = true,paramType="form"),
            @ApiImplicitParam(name = "content", value = "内容",required = true,paramType="form"),
            @ApiImplicitParam(name = "tags", value = "标签",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleCategories", value = "分类",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介",required = true,paramType="form")
    })
    @RequestRequire(require = "id,title,content,tags,articleCategories,articleSummary", parameter = Article.class)
    public ServerResponse<String> updateArticle(Article article) {
        Date date = new Date();
        article.setUpdateTime(date);


        boolean bool = articleService.updateById(article);
        if (bool) {
            return ServerResponse.success(null, "文章修改成功");
        }
        return ServerResponse.error("文章发表失败", 30001);
    }

    @ApiOperation(value="上传图片")
    @PostMapping("/upload")
    public ServerResponse upload(MultipartFile file, String post) {
        if (file.isEmpty()) {
            return ServerResponse.error("上传失败，请选择文件", 30000);
        }
        String fileName = file.getOriginalFilename();
        String postPath;
        if (StringUtils.isBlank(post)) {
            postPath = picPath;
        } else {
            postPath = picPath + post;
        }
        File postFile = new File(postPath);
        if (!postFile.exists()) {
            postFile.mkdirs();
        }
        String filePath = postPath + "/" + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            return ServerResponse.success(filePath, "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.error("上传失败", 30001);
    }

}
