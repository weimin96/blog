package com.wiblog.service;

import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *   服务类
 *
 * @author pwm
 * @since 2019-09-01
 */
public interface ICommentService extends IService<Comment> {
    /**
     * 发布评论
     * @param comment comment
     * @return ServerResponse
     */
    ServerResponse reply(Comment comment);

    /**
     * 获取评论内容
     * @param id 文章id
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param orderBy orderBy
     * @return ServerResponse
     */
    ServerResponse commentListPage(Long id,Integer pageNum,Integer pageSize,String orderBy);

    /**
     * 获取评论管理列表
     * @param articleId 文章id
     * @param title 文章标题模糊查询
     * @param state 评论状态
     * @param username 用户名模糊查询
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param orderBy orderBy
     * @return ServerResponse
     **/
    ServerResponse commentManageListPage(Long articleId, String title, Integer state, String username, Integer pageNum, Integer pageSize, String orderBy);
}
