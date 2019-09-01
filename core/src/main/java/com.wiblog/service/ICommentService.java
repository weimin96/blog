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
}
