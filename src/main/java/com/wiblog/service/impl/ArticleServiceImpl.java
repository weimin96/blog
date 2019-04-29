package com.wiblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiblog.common.ResultConstant;
import com.wiblog.common.ServerResponse;
import com.wiblog.dao.ArticleMapper;
import com.wiblog.model.Article;
import com.wiblog.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pwm
 * @date 2019/4/13
 */
@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ServerResponse<PageInfo> findAllArticle(Integer num, Integer size) {
        PageHelper.startPage(num,size);
        List<Article> articleList = articleMapper.selectAllArticle();
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        pageInfo.setList(articleList);
        return ServerResponse.success(pageInfo, ResultConstant.Article.GET_ARTICLE_LIST_SUCCESS);
    }
}
