package com.wiblog.dao;

import com.wiblog.model.Article;

import java.util.List;

/**
 * @author pwm
 */
public interface ArticleMapper {

    /**
     * 新增文章
     * @param article article
     * @return int
     */
    int insert(Article article);

    /**
     * 通过文章id更新文章
     * @param article article
     * @return int
     */
    int updateByArticleIdSelective(Article article);

    /**
     * TODO 查找所有文章 按日期降序 List<Map<String, Object>>
     * @return List<Article>
     */
    List<Article> selectAllArticle();
}