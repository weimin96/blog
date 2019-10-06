package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.Article;

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
}
