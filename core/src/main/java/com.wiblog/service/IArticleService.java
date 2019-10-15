package com.wiblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Article;
import com.wiblog.entity.User;
import com.wiblog.vo.ArticleDetailVo;

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
     * 获取文章列表
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

    /**
     * 通过url获取文章信息
     * @param url url
     * @param user user
     * @return ServerResponse
     */
    ServerResponse<ArticleDetailVo> getArticle(String url, User user);

    /**
     * 获取文章管理列表
     * @param num num
     * @param size size
     * @return ServerResponse
     */
    ServerResponse<IPage> articlesManage(Integer num, Integer size);
}
