package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.Article;

import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-06-12
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 评论数+1
     * @param articleId articleId
     * @return int
     */
    int updateCommentCount(Long articleId);

    /**
     * 获取所有文章
     * @return List<String>
     */
    List<Map<String,String>> selectAllArticle();
}
