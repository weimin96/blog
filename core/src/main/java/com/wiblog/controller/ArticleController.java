package com.wiblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.aop.OpsRecord;
import com.wiblog.aop.RequestRequire;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;
import com.wiblog.entity.User;
import com.wiblog.es.EsArticle;
import com.wiblog.es.EsArticleRepository;
import com.wiblog.service.IArticleService;
import com.wiblog.utils.PinYinUtil;
import com.wiblog.utils.WiblogUtil;
import com.wiblog.utils.WordFilterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
public class ArticleController extends BaseController {

    private static final Pattern PATTERN_HIGH_LIGHT= Pattern.compile("<(font)[^>]*>(.*?)<\\/\\1>");

    private IArticleService articleService;

    @Autowired
    private WordFilterUtil wordFilterUtil;

    @Autowired
    private PinYinUtil pinYinUtil;

    @Autowired
    private EsArticleRepository articleRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    @ApiOperation(value = "文章列表", notes = "获取文章分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认0）", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量(默认10)", paramType = "form")
    })
    public ServerResponse<IPage> articlePageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.articlePageList(pageNum, pageSize);
    }

    @PostMapping("/articlesManage")
    @ApiOperation(value = "文章列表", notes = "获取文章管理列表")
    @AuthorizeCheck(grade = "2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认0）", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量(默认10)", paramType = "form")
    })
    public ServerResponse<IPage> articlesManage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return articleService.articlesManage(pageNum, pageSize);
    }

    /**
     * 获取所有文章标题列表 管理员权限
     *
     * @return ServerResponse
     */
    @PostMapping("/allArticles")
    @AuthorizeCheck(grade = "2")
    @ApiOperation(value = "所有文章标题列表", notes = "获取文章标题列表")
    public ServerResponse articlePageList() {
        return articleService.getAllArticle();
    }

    @ApiOperation(value = "通过url的文章id获取文章详细内容")
    @GetMapping("/get/{id}")
    public ServerResponse getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @ApiOperation(value = "通过url获取文章详细内容")
    @GetMapping("/getArticle")
    public ServerResponse getArticle(HttpServletRequest request, String url) {
        User user = getLoginUser(request);
        return articleService.getArticle(url, user);
    }

    /**
     * 发表文章 管理员权限
     *
     * @param article article
     * @return ServerResponse
     */
    @ApiOperation(value = "发表文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "form"),
            @ApiImplicitParam(name = "tags", value = "标签", required = true, paramType = "form"),
            @ApiImplicitParam(name = "categoryId", value = "分类", required = true, paramType = "form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介", required = true, paramType = "form")
    })
    @PostMapping("/push")
    @AuthorizeCheck(grade = "2")
    @OpsRecord(msg = "发表了文章<<{0}>>")
    @RequestRequire(require = "title,content,tags,categoryId,imgUrl", parameter = Article.class)
    public ServerResponse<String> pushArticle(HttpServletRequest request, Article article) {

        // 分词
        List<String> titles = wordFilterUtil.getParticiple(article.getTitle());
        String title = pinYinUtil.getStringPinYin(titles);
        String articleUrl = "/post/" + title;
        Article sameUrlArticle = articleService.getOne(new QueryWrapper<Article>().eq("article_url", articleUrl));
        if (sameUrlArticle != null) {
            return ServerResponse.error("文章发表失败，已存在相同标题", 30001);
        }
        Date date = new Date();
        article.setUpdateTime(date);
        article.setCreateTime(date);
        article.setArticleUrl(articleUrl);
        article.setHits(0);
        // 提取纯文本
        String content = WiblogUtil.mdToHtml(article.getContent());
        content = content.replaceAll("<[^>]+>","");
        content = content.replaceAll("\\s*|\t|\r|\n","");
        article.setArticleSummary(content.substring(0,100));
        User user = getLoginUser(request);
        article.setAuthor(user.getUsername());
        article.setUid(user.getUid());
        boolean bool = articleService.save(article);

        if (bool) {
            Article article1 = articleService.getOne(new QueryWrapper<Article>().eq("title",article.getTitle()));
            articleRepository.save(new EsArticle(article1.getId(),article1.getTitle(),content,article1.getCategoryId(),article1.getCreateTime(),article1.getArticleUrl()));
            return ServerResponse.success(articleUrl, "文章发表成功", title);
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
    @ApiOperation(value = "修改文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "id", value = "文章id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "form"),
            @ApiImplicitParam(name = "tags", value = "标签", required = true, paramType = "form"),
            @ApiImplicitParam(name = "categoryId", value = "分类", required = true, paramType = "form"),
            @ApiImplicitParam(name = "articleSummary", value = "简介", required = true, paramType = "form")
    })
    @OpsRecord(msg = "修改了文章<<{0}>>")
    @RequestRequire(require = "id,title,content,tags,categoryId", parameter = Article.class)
    public ServerResponse<String> updateArticle(Article article) {
        Date date = new Date();
        article.setUpdateTime(date);
        // 提取纯文本
        String content = WiblogUtil.mdToHtml(article.getContent());
        content = content.replaceAll("<[^>]+>","");
        content = content.replaceAll("\\s*|\t|\r|\n","");
        article.setArticleSummary(content.substring(0,100));
        boolean bool = articleService.updateById(article);
        if (bool) {
            EsArticle esArticle = new EsArticle(article.getId(),article.getTitle(),article.getContent(),article.getCategoryId(),article.getCreateTime(),article.getArticleUrl());
            articleRepository.save(esArticle);
            return ServerResponse.success(null, "文章修改成功",article.getTitle());
        }
        return ServerResponse.error("文章发表失败", 30001);
    }

    @PostMapping("/del")
    @AuthorizeCheck(grade = "2")
    @OpsRecord(msg = "删除了文章<<{0}>>")
    public ServerResponse delArticle(Long id) {
        articleRepository.deleteByArticleId(id);
        return articleService.delArticle(id);
    }

    /**
     * 全文检索
     * @param keyword keyword
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return ServerResponse
     */
    @GetMapping("/searchArticle")
    public ServerResponse searchArticle(@RequestParam(required = false, defaultValue = "") String keyword,
                                        @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        keyword = keyword.trim();
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        //高亮拼接的前缀
        String preTags="<font color=\"red\">";
        //高亮拼接的后缀
        String postTags="</font>";
        //查询具体的字段
        String[] fieldNames= {"title","content"};
        //创建queryBuilder查询条件
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, fieldNames);
        //创建search对象
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).withHighlightFields(
                new HighlightBuilder.Field(fieldNames[0]).preTags(preTags).postTags(postTags),
                new HighlightBuilder.Field(fieldNames[1]).preTags(preTags).postTags(postTags)
        ).withPageable(pageable).build();

        //执行分页查询
        Page<EsArticle> page = elasticsearchTemplate.queryForPage(query,EsArticle.class,new SearchResultMapper(){
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                List<EsArticle> list = new ArrayList<>();
                // 获取高亮结果
                SearchHits searchHits = response.getHits();
                if (searchHits != null){
                    // 获取内容
                    SearchHit[] hits = searchHits.getHits();
                    if (hits.length>0){
                        for (SearchHit hit:hits){
                            EsArticle esArticle = new EsArticle();
                            esArticle.setId(hit.getId());
                            esArticle.setUrl((String) hit.getSourceAsMap().get("url"));
                            // 获取第一个字段高亮内容
                            HighlightField highlightField1 = hit.getHighlightFields().get(fieldNames[0]);
                            String value1;
                            if (highlightField1!=null){
                                value1 = highlightField1.getFragments()[0].toString();
                            }else {
                                value1 = (String) hit.getSourceAsMap().get(fieldNames[0]);
                            }
                            esArticle.setTitle(value1);
                            // 获取第二个字段高亮内容
                            HighlightField highlightField2 = hit.getHighlightFields().get(fieldNames[1]);
                            String value2;
                            if (highlightField2!=null){
                                value2 = highlightField2.getFragments()[0].toString();
                            }else {
                                value2 = (String) hit.getSourceAsMap().get(fieldNames[1]);
                            }
                            // 截取部分内容
                            Matcher mt = PATTERN_HIGH_LIGHT.matcher(value2);
                            if (mt.find()){
                                int mtStart = mt.start();
                                int start = Math.max(mtStart - 20, 0);
                                int end = Math.min(mtStart+200,value2.length()-1);
                                value2 = value2.substring(start,end);
                            }else {
                                value2 = value2.substring(0,Math.min(200,value2.length()-1));
                            }
                            esArticle.setContent(value2);
                            list.add(esArticle);
                        }
                    }
                }
                return new AggregatedPageImpl<>((List<T>)list);
            }
        });

        return ServerResponse.success(page);
    }



}
