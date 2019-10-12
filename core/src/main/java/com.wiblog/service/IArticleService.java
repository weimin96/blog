package com.wiblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;

/**
 *   服务类
 *
 * @author pwm
 * @since 2019-06-12
 */
public interface IArticleService extends IService<Article> {

    /**
     * 获取所有文章列表
     * @return ServerResponse
     */
    ServerResponse getAllArticle();

    /**
     * 获取文章分类列表
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return ServerResponse
     */
    ServerResponse<IPage> articlePageList(Integer pageNum, Integer pageSize);

    /**
     * 获取文章详细信息
     * @param id id
     * @return ServerResponse
     */
    ServerResponse getArticleById(Long id);
}
