package com.wiblog.service.impl;

import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Comment;
import com.wiblog.mapper.CommentMapper;
import com.wiblog.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-09-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Override
    public ServerResponse reply(Comment comment) {
        Date date = new Date();
        comment.setId(null);
        comment.setLikes(0);
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        // 没有回复id是回复文章
        if (comment.getAnswererId() == null){
            comment.setAnswererId(0L);
        }int count = commentMapper.insert(comment);
        if (count <=0){
            return ServerResponse.error("评论失败",40001);
        }
        return ServerResponse.success(null,"评论成功");
    }
}
