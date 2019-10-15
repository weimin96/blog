package com.wiblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.aop.RequestRequire;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;
import com.wiblog.entity.User;
import com.wiblog.service.IArticleService;
import com.wiblog.utils.PinYinUtil;
import com.wiblog.utils.WordFilterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


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
public class ArticleController extends BaseController{

    private IArticleService articleService;

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
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.articlePageList(pageNum,pageSize);
    }

    @PostMapping("/articlesManage")
    @ApiOperation(value="文章列表", notes="获取文章管理列表")
    @AuthorizeCheck(grade = "2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认0）",paramType="form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量(默认10)",paramType="form")
    })
    public ServerResponse<IPage> articlesManage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.articlesManage(pageNum,pageSize);
    }

    /**
     * 获取所有文章标题列表 管理员权限
     * @return ServerResponse
     */
    @PostMapping("/allArticles")
    @AuthorizeCheck(grade = "2")
    @ApiOperation(value="所有文章标题列表", notes="获取文章标题列表")
    public ServerResponse articlePageList() {
        return articleService.getAllArticle();
    }

    @ApiOperation(value="通过url的文章id获取文章详细内容")
    @GetMapping("/get/{id}")
    public ServerResponse getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @ApiOperation(value="通过url获取文章详细内容")
    @GetMapping("/getArticle")
    public ServerResponse getArticle(HttpServletRequest request,String url) {
        User user = getLoginUser(request);
        return articleService.getArticle(url,user);
    }

    /**
     * 发表文章 管理员权限
     *
     * @param article article
     * @return ServerResponse
     */
    @ApiOperation(value="发表文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题",required = true,paramType="form"),
            @ApiImplicitParam(name = "content", value = "内容",required = true,paramType="form"),
            @ApiImplicitParam(name = "tags", value = "标签",required = true,paramType="form"),
            @ApiImplicitParam(name = "categoryId", value = "分类",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介",required = true,paramType="form")
    })
    @PostMapping("/push")
    @AuthorizeCheck(grade = "2")
    @RequestRequire(require = "title,content,tags,articleSummary,categoryId,imgUrl", parameter = Article.class)
    public ServerResponse<String> pushArticle(HttpServletRequest request,Article article) {
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
        article.setHits(0);
        User user = getLoginUser(request);
        article.setAuthor(user.getUsername());
        boolean bool = articleService.save(article);

        if (bool) {
            return ServerResponse.success(articleUrl, "文章发表成功");
        }
        return ServerResponse.error("文章发表失败", 30001);
    }

    /**
     * 修改文章 管理员权限
     *
     * @param article article
     * @return ServerResponse
     */
    @PostMapping("/update")
    @AuthorizeCheck(grade = "2")
    @ApiOperation(value="修改文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题",required = true,paramType="form"),
            @ApiImplicitParam(name = "id", value = "文章id",required = true,paramType="form"),
            @ApiImplicitParam(name = "content", value = "内容",required = true,paramType="form"),
            @ApiImplicitParam(name = "tags", value = "标签",required = true,paramType="form"),
            @ApiImplicitParam(name = "categoryId", value = "分类",required = true,paramType="form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介",required = true,paramType="form")
    })
    @RequestRequire(require = "id,title,content,tags,categoryId,articleSummary", parameter = Article.class)
    public ServerResponse<String> updateArticle(Article article) {
        Date date = new Date();
        article.setUpdateTime(date);
        boolean bool = articleService.updateById(article);
        if (bool) {
            return ServerResponse.success(null, "文章修改成功");
        }
        return ServerResponse.error("文章发表失败", 30001);
    }

}
