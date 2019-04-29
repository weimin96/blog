package com.wiblog.service;

import com.github.pagehelper.PageInfo;
import com.wiblog.common.ServerResponse;

/**
 * @author pwm
 * @date 2019/4/13
 */
public interface ArticleService {


    /**
     * 分页获取最新文章列表
     * @param num 页码
     * @param size 页数
     * @return ServerResponse
     */
    ServerResponse<PageInfo> findAllArticle(Integer num, Integer size);
}
